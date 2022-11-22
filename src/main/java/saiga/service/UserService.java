package saiga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import saiga.dto.SignUpDto;
import saiga.model.User;
import saiga.repository.RoleRepository;
import saiga.repository.UserRepository;
import saiga.security.JwtProvider;

@Service
public class UserService {
    private final UserRepository repository;
    private final JwtProvider jwtProvider;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository repository, JwtProvider jwtProvider, RoleRepository roleRepository) {
        this.repository = repository;
        this.jwtProvider = jwtProvider;
        this.roleRepository = roleRepository;
    }

    public HttpEntity<?> login(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber)
                .map(user -> ResponseEntity.ok(jwtProvider.generateToken(phoneNumber)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public HttpEntity<?> signUp(SignUpDto dto) {
        return repository.findByPhoneNumber(dto.getPhoneNumber()).
                map(user -> ResponseEntity.status(HttpStatus.CONFLICT).build())
                .orElseGet(() -> {
                    repository.save(new User(dto.getPhoneNumber(), roleRepository.findByRole(dto.getRole())));
                    return ResponseEntity.ok(HttpStatus.CREATED);
                });
    }

    public HttpEntity<?> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}
