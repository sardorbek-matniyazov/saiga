package saiga.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import saiga.model.enums.Lang;
import saiga.model.enums.RoleEnum;
import saiga.model.enums.Status;
import saiga.payload.MyResponse;
import saiga.payload.dto.UserDTO;
import saiga.payload.request.SignUpRequest;
import saiga.repository.UserRepository;
import saiga.security.CustomUserDetailsService;
import saiga.security.JwtProvider;
import saiga.security.MyFilter;
import saiga.service.AuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 27 Feb 2023
 **/
@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {AuthController.class, UserRepository.class, AuthService.class, JwtProvider.class, MyFilter.class, CustomUserDetailsService.class})
class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signIn() {
    }

    @Test
    void signUp() throws Exception {
        // custom Request data
        final SignUpRequest signUpRequest = new SignUpRequest(
                "+998913030240",
                "Sardor",
                "Matniyazov",
                RoleEnum.USER
        );

        // expected response
        final MyResponse expectedResponse = MyResponse
                ._CREATED()
                .setMessage("User registered successfully")
                .addData(
                        "data",
                        new UserDTO(
                                1L,
                                "+998913030240",
                                "Sardor",
                                "Matniyazov",
                                Lang.KAA.name(),
                                RoleEnum.USER.name(),
                                Status.ACTIVE
                        )
                );

        when(authService.signUp(signUpRequest)).thenReturn(expectedResponse);

        // test
//        assertEquals(expectedResponse, authService.signUp(signUpRequest));
        this.mockMvc
                .perform(get("api/auth/sign-up"))
                .andExpect(result -> {
                            assertEquals(201, result.getResponse().getStatus());
                            assertEquals("application/json", result.getResponse().getContentType());
                            assertEquals(result.getResponse().getContentLength(), 1);
                        }
                );
    }

    @Test
    void update() {
    }
}