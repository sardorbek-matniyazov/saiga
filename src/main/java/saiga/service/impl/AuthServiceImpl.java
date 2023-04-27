package saiga.service.impl;

import org.springframework.stereotype.Service;
import saiga.model.Cabinet;
import saiga.model.ConfirmationCode;
import saiga.model.Role;
import saiga.model.User;
import saiga.payload.MyResponse;
import saiga.payload.mapper.CabinetDTOMapper;
import saiga.payload.mapper.UserDTOMapper;
import saiga.payload.request.ConfirmationCodeRequest;
import saiga.payload.request.SignUpRequest;
import saiga.payload.request.UpdateUserRequest;
import saiga.repository.CabinetRepository;
import saiga.repository.ConfirmationCodeRepository;
import saiga.repository.RoleRepository;
import saiga.repository.UserRepository;
import saiga.security.CustomUserDetailsService;
import saiga.security.JwtProvider;
import saiga.service.AuthService;
import saiga.service.sms.SmsSenderService;
import saiga.utils.exceptions.AlreadyExistsException;
import saiga.utils.exceptions.NotFoundException;
import saiga.utils.statics.MessageResourceHelperFunction;

import static saiga.model.enums.Status.ACTIVE;
import static saiga.payload.MyResponse.*;

@Service
public record AuthServiceImpl(
        UserRepository repository,
        CustomUserDetailsService userDetailsService,
        JwtProvider jwtProvider,
        RoleRepository roleRepository,
        UserDTOMapper userDtoMapper,
        CabinetRepository cabinetRepository,
        CabinetDTOMapper cabinetDTOMapper,
        MessageResourceHelperFunction messageResourceHelper,
        SmsSenderService smsSenderService,
        ConfirmationCodeRepository confirmationCodeRepository
) implements AuthService {
    @Override
    public MyResponse signIn(String phoneNumber) {
        repository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new NotFoundException(
                        String.format(
                                messageResourceHelper.apply("user.not_found_with_phone"),
                                phoneNumber
                        )
                )
        );

        generateCodeAndSend(phoneNumber);

        return _OK()
                .setMessage(
                        messageResourceHelper.apply("sms.confirmation_code_sent"));
    }

    @Override
    public MyResponse signUp(SignUpRequest signUpRequest) {
        if (repository.existsByPhoneNumber(signUpRequest.phoneNumber()))
            throw new AlreadyExistsException(
                    String.format(
                            messageResourceHelper.apply("user.already_exist_with_phone"),
                            signUpRequest.phoneNumber()
                    ));

        final Cabinet save = cabinetRepository.save(
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
                                null))
        );

        generateCodeAndSend(signUpRequest.phoneNumber());

        return _CREATED()
                .addData("data", cabinetDTOMapper().apply(save))
                .setMessage(
                        messageResourceHelper.apply("auth.confirmation_code_sent"));
    }

    @Override
    public MyResponse confirmCode(ConfirmationCodeRequest confirmationCodeRequest) {
        final User user = repository.findByPhoneNumber(confirmationCodeRequest.phoneNumber()).orElseThrow(
                () -> new NotFoundException(
                        String.format(
                                messageResourceHelper.apply("user.not_found_with_phone"),
                                confirmationCodeRequest.phoneNumber()
                        )
                )
        );

        final ConfirmationCode confirmationCode = confirmationCodeRepository.findById(confirmationCodeRequest.phoneNumber())
                .orElseThrow(
                        () -> new NotFoundException(
                                String.format(
                                        messageResourceHelper.apply("user.not_found_with_phone"),
                                        confirmationCodeRequest.phoneNumber()
                                )
                        )
                );

        if (confirmationCode.test(confirmationCodeRequest)){
            confirmationCodeRepository.save(confirmationCode.resetCode());

            final String token = jwtProvider.generateToken(confirmationCodeRequest.phoneNumber());
            user.setCurrentToken(token);
            user.setStatus(ACTIVE);

            return _OK()
                    .addData("user", userDtoMapper.apply(repository.save(user)))
                    .addData("token", token)
                    .setMessage(
                            messageResourceHelper.apply("auth.signIn.success"));
        } else if (confirmationCode.isTriesExceeded() || confirmationCode.isExpiredByTime()) {
            confirmationCodeRepository.save(confirmationCode.resetCode());
            return _BAD_REQUEST()
                    .setMessage(
                            messageResourceHelper.apply("auth.confirmation_code_expired"));
        } else {
            confirmationCodeRepository.save(confirmationCode.incrementTries());
            return _BAD_REQUEST()
                    .setMessage(
                            messageResourceHelper.apply("auth.confirmation_code_invalid"));
        }
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

    @Override
    public MyResponse accessDenied() {
        return _UNAUTHORIZED()
                .setMessage(
                        messageResourceHelper.apply("auth.access_denied"));
    }

    // confirmation code methods
    private void generateCodeAndSend(String phoneNumber) {
        final String code = generateCode();
        saveConfirmationCode(phoneNumber, code);
        sendUserLoginCode(phoneNumber, code);
    }

    private String generateCode() {
        return String.valueOf((int) (Math.random() * 1_000_000));
    }

    private void sendUserLoginCode(String phoneNumber, String code) {
        smsSenderService.sendUserLoginCode(phoneNumber, code);
    }

    private void saveConfirmationCode(String phoneNumber, String code) {
        confirmationCodeRepository.findById(phoneNumber)
                .ifPresentOrElse(
                        confirmationCode -> confirmationCodeRepository.save(confirmationCode.setCode(code)),
                        () -> confirmationCodeRepository.save(
                                new ConfirmationCode(phoneNumber, code)
                        )
                );
    }
}
