package saiga.service;

import saiga.payload.MyResponse;
import saiga.payload.request.SignUpRequest;
import saiga.payload.request.UpdateUserRequest;

public interface AuthService {
    MyResponse signIn(String phoneNumber);

    MyResponse signUp(SignUpRequest signUpDto);

    MyResponse update(Long id, UpdateUserRequest signUpDto);

    MyResponse accessDenied();
}