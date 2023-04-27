package saiga.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import saiga.payload.request.ConfirmationCodeRequest;
import saiga.payload.request.SignUpRequest;
import saiga.payload.request.UpdateUserRequest;
import saiga.service.AuthService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "api/auth")
public record AuthController (AuthService service) {
    @PostMapping(value = "sign-in")
    public HttpEntity<?> signIn(@RequestBody @Valid Map<String, String> mp) {
        return service.signIn(mp.get("phoneNumber")).handleResponse();
    }

    @PostMapping(value = "sign-up")
    public HttpEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return service.signUp(signUpRequest).handleResponse();
    }

    @PostMapping(value = "confirm-code")
    public HttpEntity<?> confirmCode(@RequestBody @Valid ConfirmationCodeRequest confirmationCodeRequest) {
        return service.confirmCode(confirmationCodeRequest).handleResponse();
    }

    @PutMapping(value = "{id}")
    public HttpEntity<?> update(@RequestBody @Valid UpdateUserRequest updateUserRequest, @PathVariable Long id) {
        return service.update(id, updateUserRequest).handleResponse();
    }

    @GetMapping(value = "access-denied")
    public HttpEntity<?> accessDenied() {
        return service.accessDenied().handleResponse();
    }
}
