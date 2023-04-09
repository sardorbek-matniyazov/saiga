package saiga.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import saiga.model.User;
import saiga.repository.UserRepository;

@Service
public record CustomUserDetailsService(
        UserRepository userRepository
) implements UserDetailsService {
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                String.format("User with phone number %s not found", username)));
    }
}
