package saiga.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import saiga.model.Order;
import saiga.model.enums.OrderStatus;
import saiga.repository.OrderRepository;
import saiga.service.OrderScheduleService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 11 Mar 2023
 **/
@Service
public class OrderScheduleServiceImpl implements OrderScheduleService {
    final Logger logger = Logger.getLogger(OrderScheduleServiceImpl.class.getName());

    private final OrderRepository orderRepository;

    @Autowired
    public OrderScheduleServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Scheduled(cron = "0 0/1 * * * ?")
    public void deleteOrderEachFiveMinutes() {
        final int size = orderRepository.findAllByStatus(OrderStatus.ACTIVE)
                .stream()
                .filter(order -> order.getCreatedDate().toLocalDateTime().plusMinutes(10).isBefore(LocalDateTime.now()))
                .map(order -> {
                    order.setStatus(OrderStatus.DELETED);
                    return orderRepository.save(order);
                }).toList().size();

        logger.info("Deleted " + size + " orders");
    }
}
