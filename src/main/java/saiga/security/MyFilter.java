package saiga.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import saiga.model.User;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService service;

    @Autowired
    public MyFilter(JwtProvider jwtProvider, CustomUserDetailsService service) {
        this.jwtProvider = jwtProvider;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer") && token.length() > 7) {
            token = token.substring(7);
            String username = jwtProvider.parseToken(token);
            User user;
            try {
                 user = service.loadUserByUsername(username);
                 if (user.getCurrentToken() != null && !user.getCurrentToken().equals(token))
                     throw new AuthenticationException("Token is not valid");
//                     redirectToAccessDenied(response);
            } catch (UsernameNotFoundException e) {
                filterChain.doFilter(request, response);
                return;
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
//            response.getWriter().write("Token is not valid");
//            redirectToAccessDenied(response);
        }
        filterChain.doFilter(request, response);
    }

//    private void redirectToAccessDenied(HttpServletResponse response) throws IOException {
//        response.setStatus(400);
//        response.sendRedirect("/access-denied");
//    }
}
