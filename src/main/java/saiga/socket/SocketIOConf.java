package saiga.socket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 01 Feb 2023
 **/

@Component
public class SocketIOConf {
    @Bean
    public SocketIOServer socketIOServer () {
        final Configuration configuration = new Configuration();
        configuration.setHostname("localhost");
        configuration.setPort(9092);
        return new SocketIOServer(configuration);
    }
}
