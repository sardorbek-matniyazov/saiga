package saiga.service;

import saiga.payload.MyResponse;
import saiga.payload.request.ConfirmationCodeRequest;
import saiga.payload.request.SignUpRequest;
import saiga.payload.request.UpdateUserRequest;

public interface AuthService {
    MyResponse signIn(String phoneNumber);

    MyResponse signUp(SignUpRequest signUpDto);
    MyResponse confirmCode(ConfirmationCodeRequest confirmationCodeRequest);

    MyResponse update(Long id, UpdateUserRequest signUpDto);

    MyResponse accessDenied();
}
