package saiga.service.impl;

import org.springframework.stereotype.Service;
import saiga.model.Cabinet;
import saiga.model.Role;
import saiga.model.User;
import saiga.payload.MyResponse;
import saiga.payload.mapper.CabinetDTOMapper;
import saiga.payload.mapper.UserDTOMapper;
import saiga.payload.request.SignUpRequest;
import saiga.payload.request.UpdateUserRequest;
import saiga.repository.CabinetRepository;
import saiga.repository.RoleRepository;
import saiga.repository.UserRepository;
import saiga.security.CustomUserDetailsService;
import saiga.security.JwtProvider;
import saiga.service.AuthService;
import saiga.utils.exceptions.AlreadyExistsException;
import saiga.utils.exceptions.NotFoundException;
import saiga.utils.statics.MessageResourceHelperFunction;

import static saiga.payload.MyResponse._CREATED;
import static saiga.payload.MyResponse._OK;
import static saiga.payload.MyResponse._UPDATED;

@Service
public record AuthServiceImpl(
        UserRepository repository,
        CustomUserDetailsService userDetailsService,
        JwtProvider jwtProvider,
        RoleRepository roleRepository,
        UserDTOMapper userDtoMapper,
        CabinetRepository cabinetRepository,
        CabinetDTOMapper cabinetDTOMapper,
        MessageResourceHelperFunction messageResourceHelper
) implements AuthService {
    @Override
    public MyResponse signIn(String phoneNumber) {
        final String token = jwtProvider.generateToken(phoneNumber);
        return _OK()
                .setMessage(
                        messageResourceHelper.apply("auth.signIn.success"))
                .addData(
                        "data",
                        userDtoMapper.apply(
                                repository.save(
                                    userDetailsService.loadUserByUsername(
                                        phoneNumber
                                    ).setToken(token))))
                .addData("token", token);
    }

    @Override
    public MyResponse signUp(SignUpRequest signUpRequest) {
        if (repository.existsByPhoneNumber(signUpRequest.phoneNumber()))
            throw new AlreadyExistsException(
                    String.format(
                            messageResourceHelper.apply("user.already_exist_with_phone"),
                            signUpRequest.phoneNumber()
                    ));
        final String token = jwtProvider.generateToken(signUpRequest.phoneNumber());

        return _CREATED()
                .addData("data", cabinetDTOMapper().apply(
                        cabinetRepository.save(
                                new Cabinet(
                                        new User(
                                                signUpRequest.firstName(),
                                                signUpRequest.lastName(),
                                                signUpRequest.phoneNumber(),
                                                roleRepository.findByRole(signUpRequest.role()).orElse(
                                                        new Role(
                                                                signUpRequest.role()
                                                        )
                                                ),
                                                token)))
                )).setMessage(
                        messageResourceHelper.apply("auth.sigUp.success"))
                .addData("token", token);
    }

    @Override
    public MyResponse update(Long id, UpdateUserRequest updateUserRequest) {
        if (repository.existsByPhoneNumberAndIdIsNot(updateUserRequest.phoneNumber(), id))
            throw new AlreadyExistsException(
                    String.format(
                            messageResourceHelper.apply("user.already_exist_with_phone")
                    )
            );

        final User user = repository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format(
                                messageResourceHelper.apply("user.not_found_with_id"),
                                updateUserRequest.phoneNumber()
                        )
                )
        );

        user.updateWithDto(updateUserRequest);
        final String token = jwtProvider.generateToken(updateUserRequest.phoneNumber());
        return _UPDATED()
                .addData("data", userDtoMapper.apply(repository.save(user.setToken(token))))
                .addData("token", token)
                .setMessage(
                        messageResourceHelper.apply("updated"));
    }
}
