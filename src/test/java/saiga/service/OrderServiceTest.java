package saiga.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import saiga.payload.mapper.OrderDTOMapper;
import saiga.repository.CabinetRepository;
import saiga.repository.OrderRepository;
import saiga.utils.statics.GlobalMethodsToHelp;
import saiga.utils.statics.MessageResourceHelperFunction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 11 Mar 2023
 **/
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    private OrderService orderService;
    @Mock private OrderRepository repository;
    @Mock private CabinetRepository cabinetRepository;
    @Mock private OrderDTOMapper orderDTOMapper;
    @Mock private OrderDeliverService orderDeliverSocketServiceImpl;
    @Mock private OrderDeliverService orderDeliverTelegramServiceImpl;
    @Mock private MessageResourceHelperFunction messageResourceHelper;
    private GlobalMethodsToHelp globalMethodsToHelp;

    @Test
    @Disabled
    void driversOrder() {
    }

    @Test
    @Disabled
    void usersOrder() {
    }

    @Test
    @Disabled
    void nonReceivedOrders() {
    }

    @Test
    @Disabled
    void receiveOrderById() {
    }

    @Test
    @Disabled
    void currentDriversHistoryOfOrder() {
    }

    @Test
    @Disabled
    void endOrderById() {
    }

    @Test
    @Disabled
    void cancelOwnReceivedOrderByOrderId() {
    }

    @Test
    @Disabled
    void cancelOwnNonReceivedOrderByOrderId() {
    }
}