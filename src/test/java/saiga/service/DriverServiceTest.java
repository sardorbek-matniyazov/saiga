package saiga.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import saiga.model.Cabinet;
import saiga.model.Role;
import saiga.model.User;
import saiga.model.enums.Lang;
import saiga.model.enums.RoleEnum;
import saiga.model.enums.Status;
import saiga.payload.MyResponse;
import saiga.repository.CabinetRepository;
import saiga.repository.OrderRepository;
import saiga.service.impl.DriverServiceImpl;
import saiga.utils.exceptions.NotFoundException;
import saiga.utils.statics.GlobalMethodsToHelp;
import saiga.utils.statics.MessageResourceHelperFunction;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static saiga.model.enums.OrderStatus.ORDERED;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 10 Mar 2023
 **/
@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    private DriverService underTest;
    @Mock private CabinetRepository cabinetRepository;
    @Mock private GlobalMethodsToHelp globalMethodsToHelp;
    @Mock private OrderRepository orderRepository;
    @Mock private MessageResourceHelperFunction messageResourceHelper;

    @BeforeEach
    void setUp() {
        underTest = new DriverServiceImpl(cabinetRepository, orderRepository,
                messageResourceHelper, globalMethodsToHelp);
    }

    @Test
    void canGetBalanceInOutWhenCabinetExists() {
        // given
        final User currentUser = new User(
                1L,
                "John",
                "Doe",
                Lang.ENG,
                "PHONE_NUMBER",
                "Current-Token",
                new Role(RoleEnum.USER),
                Status.ACTIVE
        );
        given(globalMethodsToHelp.getCurrentUser()).willReturn(currentUser);
        given(cabinetRepository.findByUserId(currentUser.getId())).willReturn(
                Optional.of(new Cabinet(1L, currentUser, BigDecimal.ZERO))
        );

        // when
        final MyResponse balanceInOut = underTest.getBalanceInOut();
        verify(orderRepository).sumOfBenefitByCabinetToIdAndStatus(1L, ORDERED);
        verify(orderRepository).countByCabinetToIdAndStatus(1L, ORDERED);

        // then
        assertThat(balanceInOut).isNotNull();
        assertThat(balanceInOut.getMessage()).isEqualTo("Balance in out");
        assertThat(balanceInOut.getBody().size()).isEqualTo(1);
        assertThat(balanceInOut.getBody().containsKey("data")).isTrue();
    }

    @Test
    void shouldThrownExceptionGetBalanceInOutWhenCabinetNotExists() {
        // given
        final User currentUser = new User(
                1L,
                "John",
                "Doe",
                Lang.ENG,
                "PHONE_NUMBER",
                "Current-Token",
                new Role(RoleEnum.USER),
                Status.ACTIVE
        );
        given(globalMethodsToHelp.getCurrentUser()).willReturn(currentUser);
        given(cabinetRepository.findByUserId(currentUser.getId())).willReturn(
                Optional.empty());
        given(messageResourceHelper.apply("cabinet.not_found_with_user_id"))
                .willReturn("Cabinet not found with user id 1");

        // when
        verify(orderRepository, never()).sumOfBenefitByCabinetToIdAndStatus(1L, ORDERED);
        verify(orderRepository, never()).countByCabinetToIdAndStatus(1L, ORDERED);

        // then
        assertThatThrownBy(() -> underTest.getBalanceInOut())
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cabinet not found with user id " + currentUser.getId());
    }
}