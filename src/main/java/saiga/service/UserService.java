package saiga.service;

import saiga.payload.dao.MyResponse;
import saiga.payload.dto.SignUpDto;
import saiga.payload.dto.UpdateUserDto;

public interface UserService {
    MyResponse login(String phoneNumber);

    MyResponse signUp(SignUpDto signUpDto);

    MyResponse update(Long id, UpdateUserDto signUpDto);
}
