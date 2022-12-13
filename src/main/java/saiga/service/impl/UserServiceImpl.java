package saiga.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saiga.model.Role;
import saiga.model.User;
import saiga.payload.dao.MyResponse;
import saiga.payload.dto.SignUpDto;
import saiga.payload.dto.UpdateUserDto;
import saiga.repository.RoleRepository;
import saiga.repository.UserRepository;
import saiga.security.JwtProvider;
import saiga.security.CustomUserDetailsService;
import saiga.service.UserService;
import saiga.utils.exceptions.AlreadyExistsException;
import saiga.utils.exceptions.NotFoundException;

import static saiga.payload.dao.MyResponse._CREATED;
import static saiga.payload.dao.MyResponse._UPDATED;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final CustomUserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository repository,
            JwtProvider jwtProvider,
            CustomUserDetailsService userDetailsService,
            RoleRepository roleRepository
    ) {
        this.repository = repository;
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
        this.roleRepository = roleRepository;
    }

    @Override
    public MyResponse login(String phoneNumber) {
        final String token = jwtProvider.generateToken(phoneNumber);
        return _CREATED
                .setMessage("Login successfully")
                .addData(
                        "user",
                        repository.save(
                                userDetailsService.loadUserByUsername(
                                        phoneNumber
                                ).setToken(token)
                        )
                ).addData("token", token);
    }

    @Override
    public MyResponse signUp(SignUpDto signUpDto) {
        if (repository.existsByPhoneNumber(signUpDto.getPhoneNumber()))
            throw new AlreadyExistsException("User with phone number " + signUpDto.getPhoneNumber() + " already exists");
        final String token = jwtProvider.generateToken(signUpDto.getPhoneNumber());
        return _CREATED
                .addData("data", repository.save(
                        new User(
                                signUpDto.getPhoneNumber(),
                                roleRepository.findByRole(signUpDto.getRole()).orElse(
                                        roleRepository.save(
                                                new Role(
                                                        signUpDto.getRole()
                                                )
                                        )
                                ),
                                token
                        )
                )).setMessage("Sign Up successfully")
                .addData("token", token);
    }

    @Override
    public MyResponse update(Long id, UpdateUserDto updateUserdto) {
        if (repository.existsByPhoneNumberAndIdIsNot(updateUserdto.getPhoneNumber(), id))
            throw new AlreadyExistsException("User with phone number " + updateUserdto.getPhoneNumber() + " already exists");

        final User user = repository.findById(id).orElseThrow(
                () -> new NotFoundException("User with id " + id + " not found")
        );

        user.updateWithDto(updateUserdto);
        final String token = jwtProvider.generateToken(updateUserdto.getPhoneNumber());
        return _UPDATED
                .addData("data", repository.save(user.setToken(token)))
                .addData("token", token)
                .setMessage("Updated successfully")
                .addData("token", token);
    }
}
