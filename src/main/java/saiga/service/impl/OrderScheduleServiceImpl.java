package saiga.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import saiga.model.enums.OrderStatus;
import saiga.model.enums.OrderType;
import saiga.payload.dto.OrderDTO;
import saiga.payload.mapper.OrderDTOMapper;
import saiga.repository.OrderRepository;
import saiga.service.OrderDeliverService;
import saiga.service.OrderScheduleService;

import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 11 Mar 2023
 **/
@Service
public class OrderScheduleServiceImpl implements OrderScheduleService {
    final Logger logger = Logger.getLogger(OrderScheduleServiceImpl.class.getName());

    private final OrderRepository orderRepository;
    private final OrderDeliverService orderDeliverSocketServiceImpl;
    private final OrderDeliverService orderDeliverTelegramServiceImpl;
    private final OrderDTOMapper orderDTOMapper;

    @Autowired
    public OrderScheduleServiceImpl(OrderRepository orderRepository, OrderDeliverService orderDeliverSocketServiceImpl, OrderDeliverService orderDeliverTelegramServiceImpl, OrderDTOMapper orderDTOMapper) {
        this.orderRepository = orderRepository;
        this.orderDeliverSocketServiceImpl = orderDeliverSocketServiceImpl;
        this.orderDeliverTelegramServiceImpl = orderDeliverTelegramServiceImpl;
        this.orderDTOMapper = orderDTOMapper;
    }

    @Override
    @Scheduled(cron = "0 0/5 * * * *")
    public void deleteOrderEachFiveMinutes() {
        final int size = orderRepository.findAllByStatus(OrderStatus.ACTIVE)
                .stream()
                .filter(order -> order.getCreatedDate().toLocalDateTime().plusMinutes(10).isBefore(LocalDateTime.now()))
                .map(order -> {
                    order.setStatus(OrderStatus.DELETED);
                    // save order
                    order = orderRepository.save(order);

                    // make orderDTO
                    orderDTOMapper.apply(order);

                    // emit deleted order to socket
                    emitDeletedOrderToSocket(orderDTOMapper.apply(order), order.getType());

                    // emit deleted order to telegram
                    emitDeletedOrderToTelegram(orderDTOMapper.apply(order), order.getType());

                    return order;
                }).toList().size();

        logger.info("Deleted " + size + " orders");
    }


    private void emitDeletedOrderToSocket(OrderDTO orderDTO, OrderType type) {
        orderDeliverSocketServiceImpl.sendCanceledOrderToClient(orderDTO, type);
    }

    private void emitDeletedOrderToTelegram(OrderDTO orderDTO, OrderType type) {
        orderDeliverTelegramServiceImpl.sendCanceledOrderToClient(orderDTO, type);
    }
}
