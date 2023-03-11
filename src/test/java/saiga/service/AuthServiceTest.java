package saiga.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import saiga.model.Cabinet;
import saiga.model.Role;
import saiga.model.User;
import saiga.model.enums.Lang;
import saiga.model.enums.RoleEnum;
import saiga.model.enums.Status;
import saiga.payload.MyResponse;
import saiga.payload.dto.UserDTO;
import saiga.payload.mapper.CabinetDTOMapper;
import saiga.payload.mapper.UserDTOMapper;
import saiga.payload.request.SignUpRequest;
import saiga.payload.request.UpdateUserRequest;
import saiga.repository.CabinetRepository;
import saiga.repository.RoleRepository;
import saiga.repository.UserRepository;
import saiga.security.CustomUserDetailsService;
import saiga.security.JwtProvider;
import saiga.service.impl.AuthServiceImpl;
import saiga.utils.exceptions.AlreadyExistsException;
import saiga.utils.exceptions.NotFoundException;
import saiga.utils.statics.MessageResourceHelperFunction;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 09 Mar 2023
 **/
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock private UserRepository repository;
    @Mock private CustomUserDetailsService userDetailsService;
    @Mock private JwtProvider jwtProvider;
    @Mock private RoleRepository roleRepository;
    @Mock private UserDTOMapper userDtoMapper;
    @Mock private CabinetRepository cabinetRepository;
    @Mock private CabinetDTOMapper cabinetDTOMapper;
    @Mock private MessageResourceHelperFunction messageResourceHelper;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(
                repository,
                userDetailsService,
                jwtProvider,
                roleRepository,
                userDtoMapper,
                cabinetRepository,
                cabinetDTOMapper,
                messageResourceHelper
        );
    }

    @Test
    void defaultContextLoads() {
        assertThat(authService).isNotNull();
    }

    @Test
    void canSignInWhenExistedUser() {
        // given
        final String PHONE_NUMBER = "998977777777";
        final User givenUser = new User(
                "John",
                "Doe",
                PHONE_NUMBER,
                new Role(RoleEnum.USER),
                "1234567890"
        );
        given(userDetailsService.loadUserByUsername(PHONE_NUMBER)).willReturn(givenUser);
        given(messageResourceHelper.apply("auth.signIn.success")).willReturn("Sign in successfully");

        // when
        final MyResponse myResponse = authService.signIn(PHONE_NUMBER);
        verify(jwtProvider).generateToken(PHONE_NUMBER);
        final User savedUser = verify(repository).save(userDetailsService.loadUserByUsername(PHONE_NUMBER));
        final UserDTO appliedUserDto = verify(userDtoMapper).apply(savedUser);
        verify(messageResourceHelper).apply("auth.signIn.success");

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userArgumentCaptor.capture());

        final User captorValue = userArgumentCaptor.getValue();
        assertThat(captorValue).isNotNull();
        assertThat(captorValue.getPhoneNumber()).isEqualTo(PHONE_NUMBER);

        assertThat(myResponse).isNotNull();
        assertThat(myResponse.getCode()).isEqualTo(200);
        assertThat(myResponse.getBody()).isNotNull();
        assertThat(myResponse.getBody().size()).isEqualTo(2);
        assertThat(myResponse.isActive()).isTrue();
        assertThat(myResponse.getBody().containsKey("data")).isTrue();
        assertThat(myResponse.getBody().get("data")).isEqualTo(appliedUserDto);
    }

    @Test
    void shouldThrownExceptionWhenNonExistedUserSignIn() {
        // given
        final String PHONE_NUMBER = "998977777777";
        given(userDetailsService.loadUserByUsername(PHONE_NUMBER))
                .willThrow(new UsernameNotFoundException("User with phone number " + PHONE_NUMBER + " not found"));

        // when
        // then
        assertThatThrownBy(() -> authService.signIn(PHONE_NUMBER))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with phone number " + PHONE_NUMBER + " not found");
    }

    @Test
    void shouldThrowsExceptionSignUpWhenPhoneNumberExists() {
        // given
        SignUpRequest givenUser = new SignUpRequest(
                "John",
                "Doe",
                "998977777777",
                RoleEnum.USER
        );

        given(messageResourceHelper.apply("user.already_exist_with_phone"))
                .willReturn("User with phone number %s already exists");
        given(repository.existsByPhoneNumber(givenUser.phoneNumber())).willReturn(true);

        // when
        verify(repository, never()).save(any());
        verify(messageResourceHelper, never()).apply("auth.sigUp.success");
        verify(roleRepository, never()).findByRole(any());

        // then
        assertThatThrownBy(() -> authService.signUp(givenUser))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining(String.format("User with phone number %s already exists", givenUser.phoneNumber()));
    }

    @Test
    void canSignUpWhenNonExistedUser() {
        // given
        final String PHONE_NUMBER = "998977777777";
        SignUpRequest givenUser = new SignUpRequest(
                PHONE_NUMBER,
                "John",
                "Doe",
                RoleEnum.USER
        );

        given(messageResourceHelper.apply("auth.sigUp.success"))
                .willReturn("Sign Up successfully");

        // when
        final MyResponse myResponse = authService.signUp(givenUser);

        // then
        ArgumentCaptor<Cabinet> cabinetArgumentCaptor = ArgumentCaptor.forClass(Cabinet.class);
        verify(cabinetRepository).save(cabinetArgumentCaptor.capture());

        final Cabinet cabinet = cabinetArgumentCaptor.getValue();

        assertThat(myResponse).isNotNull();
        assertThat(myResponse.getCode()).isEqualTo(201);
        assertThat(myResponse.getBody()).isNotNull();
        assertThat(myResponse.getBody().size()).isEqualTo(2);
        assertThat(myResponse.isActive()).isTrue();
        assertThat(myResponse.getBody().containsKey("data")).isTrue();
        assertThat(myResponse.getBody().containsKey("token")).isTrue();
        assertThat(myResponse.getMessage()).isEqualTo("Sign Up successfully");
        assertThat(cabinet.getUser().getPhoneNumber()).isEqualTo(PHONE_NUMBER);
        assertThat(cabinet.getBalance()).isEqualTo("100000");
    }

    @Test
    void canUpdateUserWhenEverythingOk() {
        // given
        final Long id = 1L;
        final UpdateUserRequest updateUserRequest = new UpdateUserRequest(
                "John",
                "Doe",
                Lang.UZ,
                "+998912223344"
        );
        given(messageResourceHelper.apply("updated"))
                .willReturn("Updated successfully");
        given(repository.findById(id)).willReturn(
                Optional.of(
                        new User(
                                id,
                                "Janna",
                                "Elizabeth",
                                Lang.ENG,
                                "1234567",
                                "default-token",
                                new Role(RoleEnum.USER),
                                Status.ACTIVE
                        )
                )
        );

        // when
        final MyResponse myResponse = authService.update(id, updateUserRequest);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userArgumentCaptor.capture());

        final User captorValue = userArgumentCaptor.getValue();
        assertThat(captorValue).isNotNull();
        assertThat(captorValue.getLang()).isEqualTo(updateUserRequest.lang());
        assertThat(captorValue.getFirstName()).isEqualTo(updateUserRequest.firstName());
        assertThat(captorValue.getLastName()).isEqualTo(updateUserRequest.lastName());
        assertThat(captorValue.getPhoneNumber()).isEqualTo(updateUserRequest.phoneNumber());

        assertThat(myResponse.getCode()).isEqualTo(200);
        assertThat(myResponse.getBody()).isNotNull();
        assertThat(myResponse.getBody().size()).isEqualTo(2);
        assertThat(myResponse.isActive()).isTrue();
        assertThat(myResponse.getBody().containsKey("data")).isTrue();
        assertThat(myResponse.getBody().containsKey("token")).isTrue();
        assertThat(myResponse.getMessage()).isEqualTo("Updated successfully");
    }

    @Test
    void shouldThrownExceptionWhenUserCredentialsExists() {
        // given
        final Long id = 1L;
        final UpdateUserRequest updateUserRequest = new UpdateUserRequest(
                "John",
                "Doe",
                Lang.UZ,
                "+998912223344"
        );
        given(messageResourceHelper.apply("user.already_exist_with_phone"))
                .willReturn("User with phone number +998912223344 already exists");
        given(repository.existsByPhoneNumberAndIdIsNot(updateUserRequest.phoneNumber(), id)).willReturn(true);

        // when
        verify(repository, never()).save(any());
        verify(repository, never()).findById(any());
        verify(jwtProvider, never()).generateToken(any());
        verify(userDtoMapper, never()).apply(any());

        // then
        assertThatThrownBy(() -> authService.update(id, updateUserRequest))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining(String.format("User with phone number %s already exists", updateUserRequest.phoneNumber()));
    }

    @Test
    void shouldThrownExceptionWhenUserNotFoundWithId() {
        // given
        final Long id = 1L;
        final UpdateUserRequest updateUserRequest = new UpdateUserRequest(
                "John",
                "Doe",
                Lang.UZ,
                "+998912223344"
        );
        given(messageResourceHelper.apply("user.not_found_with_id"))
                .willReturn("User not found with id 1");
        given(repository.existsByPhoneNumberAndIdIsNot(updateUserRequest.phoneNumber(), id)).willReturn(false);
        given(repository.findById(id)).willReturn(Optional.empty());

        // when
        verify(repository, never()).save(any());
        verify(jwtProvider, never()).generateToken(any());
        verify(userDtoMapper, never()).apply(any());

        // then
        assertThatThrownBy(() -> authService.update(id, updateUserRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(String.format("User not found with id %s", id));
    }
}