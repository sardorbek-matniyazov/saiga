package saiga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saiga.payload.dto.SignUpDto;
import saiga.payload.dto.UpdateUserDto;
import saiga.service.UserService;

import javax.validation.Valid;
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
    public HttpEntity<?> login(@RequestBody @Valid Map<String, String> mp) {
        return service.login(mp.get("phoneNumber")).handleResponse();
    }

    @PostMapping(value = "/sign-up")
    public HttpEntity<?> signUp(@RequestBody @Valid SignUpDto signUpDto) {
        return service.signUp(signUpDto).handleResponse();
    }

    @PutMapping(value = "/{id}")
    public HttpEntity<?> update(@RequestBody @Valid UpdateUserDto updateUserDto, @PathVariable Long id) {
        return service.update(id, updateUserDto).handleResponse();
    }
}
