package saiga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import saiga.dto.SignUpDto;
import saiga.service.UserService;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(value = "/login")
    public HttpEntity<?> login(@RequestBody Map<String, String> mp) {
        return service.login(mp.get("phoneNumber"));
    }

    @PostMapping(value = "/sign-up")
    public HttpEntity<?> signUp(@RequestBody SignUpDto signUpDto) {
        return service.signUp(signUpDto);
    }

    @GetMapping(value = "/all")
    public HttpEntity<?> test() {
        return service.getAll();
    }
}
