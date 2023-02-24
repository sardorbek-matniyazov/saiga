package saiga.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saiga.payload.request.SignUpRequest;
import saiga.payload.request.TopUpBalanceRequest;
import saiga.payload.request.UpdateUserRequest;
import saiga.service.UserService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "api/users")
public record UserController(UserService service) {
    @PostMapping(value = "sign-in")
    public HttpEntity<?> signIn(@RequestBody @Valid Map<String, String> mp) {
        return service.signIn(mp.get("phoneNumber")).handleResponse();
    }

    @PostMapping(value = "sign-up")
    public HttpEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return service.signUp(signUpRequest).handleResponse();
    }

    @PutMapping(value = "{id}")
    public HttpEntity<?> update(@RequestBody @Valid UpdateUserRequest updateUserRequest, @PathVariable Long id) {
        return service.update(id, updateUserRequest).handleResponse();
    }
}
