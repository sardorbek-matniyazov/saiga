package saiga.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import saiga.repository.UserRepository;
import saiga.security.JwtProvider;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthenticationConfig.class);

//    @Inject
//    private AuthChannelInterceptorAdapter authChannelInterceptorAdapter;
//
//    private final JwtProvider jwtDecoder;
//    private final UserRepository repository;
//
//    @Autowired
//    public WebSocketAuthenticationConfig(JwtProvider jwtDecoder, UserRepository repository) {
//        this.jwtDecoder = jwtDecoder;
//        this.repository = repository;
//    }
//
//    @Override
//    public void registerStompEndpoints(final StompEndpointRegistry registry) {
//        // Endpoints are already registered on WebSocketConfig, no need to add more.
//    }
//
//    @Override
//    public void configureClientInboundChannel(final ChannelRegistration registration) {
//        registration.setInterceptors(authChannelInterceptorAdapter);
//    }
}
