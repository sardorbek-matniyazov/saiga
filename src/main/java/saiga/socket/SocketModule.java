package saiga.socket;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saiga.model.Order;
import saiga.payload.dto.OrderDTO;
import saiga.payload.mapper.OrderDTOMapper;
import saiga.service.OrderService;

import java.util.stream.Collectors;


@Component
public class SocketModule {
    private final SocketIONamespace namespace;
    private final OrderService orderService;
    private final OrderDTOMapper orderDTOMapper;

    @Autowired
    public SocketModule(SocketIOServer server, OrderService orderService, OrderDTOMapper orderDTOMapper) {
        this.orderService = orderService;
        this.orderDTOMapper = orderDTOMapper;
        this.namespace = server.addNamespace("/order");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener("new-order", OrderDTO.class, onChatReceived());
    }
    private DataListener<OrderDTO> onChatReceived() {
        return (client, data, ackSender) -> {
            System.out.println(client.getSessionId());
            System.out.println(client.getHandshakeData().getHttpHeaders());
            //System.out.println(data);
            namespace.getAllClients().forEach(System.out::println);
            namespace.getBroadcastOperations().sendEvent(
                    "new-order",
                    orderService.getAllNotReceivedOrders()
                            .stream()
                            .map(orderDTOMapper)
                            .collect(Collectors.toList()));
        };
    }

    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            System.out.println(handshakeData.getUrl());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {};
    }

    public void sendMessageToUserByEmit(Order order) {
        namespace.getBroadcastOperations().sendEvent("new-order", orderDTOMapper.apply(order));
    }
}
