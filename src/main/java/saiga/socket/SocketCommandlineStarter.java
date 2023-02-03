package saiga.socket;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public record SocketCommandlineStarter(
        SocketIOServer server
) implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        server.start();
    }
}
