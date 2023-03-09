package saiga.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import saiga.model.Address;
import saiga.model.Cabinet;
import saiga.model.User;
import saiga.model.enums.AddressType;
import saiga.payload.MyResponse;
import saiga.payload.dto.CabinetDTO;
import saiga.payload.mapper.CabinetDTOMapper;
import saiga.payload.request.AddressRequest;
import saiga.payload.request.TopUpBalanceRequest;
import saiga.repository.AddressRepository;
import saiga.repository.CabinetRepository;
import saiga.service.impl.AdminServiceImpl;
import saiga.utils.exceptions.AlreadyExistsException;
import saiga.utils.exceptions.NotFoundException;
import saiga.utils.exceptions.TypesInError;
import saiga.utils.statics.MessageResourceHelperFunction;

import java.math.BigDecimal;
import java.util.List;
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
 * @created : 08 Mar 2023
 **/
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock private CabinetRepository cabinetRepository;
    @Mock private AddressRepository addressRepository;

    @Mock private CabinetDTOMapper cabinetDTOMapper;
    @Mock private MessageResourceHelperFunction messageResourceHelper;

    private AdminService adminService;

    @BeforeEach
    void setUp() {
        adminService = new AdminServiceImpl(cabinetRepository, addressRepository, cabinetDTOMapper, messageResourceHelper);
    }

    @Test
    void defaultSetup() {
        assertThat(cabinetRepository).isNotNull();
        assertThat(addressRepository).isNotNull();
        assertThat(cabinetDTOMapper).isNotNull();
        assertThat(messageResourceHelper).isNotNull();
        assertThat(adminService).isNotNull();
    }

    @Test
    void getAllCabinets() {
        // given
        final List<CabinetDTO> serviceAllCabinets = adminService.getAllCabinets();

        // when
        verify(cabinetRepository).findAll(Sort.by(Sort.Direction.DESC, "id"));

        // then
        assertThat(serviceAllCabinets)
                .isNotNull();
        assertThat(serviceAllCabinets.size()).isEqualTo(0);
    }

    @Test
    void shouldCreateNotExistStaticAddress() {
        // given
        final AddressRequest payload = new AddressRequest(123.0, 124.0,"New York", "Malibu");

        // when
        final MyResponse responseOfCreateStaticAddress = adminService.createStaticAddress(payload);

        // then
        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addressRepository).save(addressArgumentCaptor.capture());
        verify(messageResourceHelper).apply("address.static.created");

        final Address capturedAddress = addressArgumentCaptor.getValue();

        assertThat(capturedAddress).isNotNull();
        assertThat(capturedAddress.getTitle()).isEqualTo(payload.title());
        assertThat(capturedAddress.getLatitude()).isEqualTo(payload.lat());
        assertThat(capturedAddress.getLongitude()).isEqualTo(payload.lon());
        assertThat(capturedAddress.getDistrict()).isEqualTo(payload.district());
        assertThat(capturedAddress.getAddressType()).isEqualTo(AddressType.STATIC);

        assertThat(responseOfCreateStaticAddress).isNotNull();
        assertThat(responseOfCreateStaticAddress.getBody().size()).isEqualTo(1);
    }

    @Test
    void shouldThrownExceptionExistedStaticAddress() {
        // given
        final AddressRequest payload = new AddressRequest(123.0, 124.0,"New York", "Malibu");

        given(addressRepository
                .existsByTitleAndDistrictAndAddressType(
                        payload.title(), payload.district(), AddressType.STATIC))
                .willReturn(true);
        given(messageResourceHelper.apply("address.static.already_exists"))
                .willReturn("Address already exists %s %s");
        verify(addressRepository, never()).save(any());

        // when

        // then
        assertThatThrownBy(() -> adminService.createStaticAddress(payload))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Address already exists");

    }

    @Test
    void getAllStaticAddresses() {
        // given
        final List<Address> serviceAllStaticAddresses = adminService.getAllStaticAddresses();

        // when
        verify(addressRepository).findAllByAddressType(AddressType.STATIC);

        // then
        assertThat(serviceAllStaticAddresses)
                .isNotNull();
        assertThat(serviceAllStaticAddresses.size()).isEqualTo(0);
    }

    @Test
    void shouldTopUpBalanceWhenCabinetExists() {
        // given
        final TopUpBalanceRequest topUpBalanceRequest = new TopUpBalanceRequest("100.0", 1L);
        final Cabinet givenCabinet = new Cabinet(
                1L,
                new User(),
                BigDecimal.valueOf(200.0)
        );
        given(cabinetRepository.findByUserId(topUpBalanceRequest.userID()))
                .willReturn(Optional.of(givenCabinet));

        // when
        final MyResponse responseOfTopUpBalance = adminService.topUpBalance(topUpBalanceRequest);

        // then
        ArgumentCaptor<Cabinet> cabinetArgumentCaptor = ArgumentCaptor.forClass(Cabinet.class);
        verify(cabinetRepository).save(cabinetArgumentCaptor.capture());
        verify(messageResourceHelper).apply("transfer_success");

        final Cabinet capturedCabinet = cabinetArgumentCaptor.getValue();

        assertThat(capturedCabinet).isNotNull();
        assertThat(capturedCabinet.getBalance()).isEqualTo(BigDecimal.valueOf(300.0));

        assertThat(responseOfTopUpBalance).isNotNull();
        assertThat(responseOfTopUpBalance.getBody().size()).isEqualTo(1);
    }

    @Test
    void shouldThrownExceptionWhenCabinetNotExists() {
        // given
        final TopUpBalanceRequest topUpBalanceRequest = new TopUpBalanceRequest("100.0", 1L);
        given(cabinetRepository.findByUserId(topUpBalanceRequest.userID()))
                .willReturn(Optional.empty());
        given(messageResourceHelper.apply("user.not_found_with_id"))
                .willReturn("Cabinet not found");
        verify(cabinetRepository, never()).save(any());

        // when

        // then
        assertThatThrownBy(() -> adminService.topUpBalance(topUpBalanceRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cabinet not found");
    }

    @Test
    void shouldThrownExceptionWhenRequestAmountIsNotValid() {
        // given
        final TopUpBalanceRequest topUpBalanceRequest = new TopUpBalanceRequest("100FFFFF", 1L);
        final Cabinet givenCabinet = new Cabinet(
                1L,
                new User(),
                BigDecimal.valueOf(200.0)
        );
        given(cabinetRepository.findByUserId(topUpBalanceRequest.userID()))
                .willReturn(Optional.of(givenCabinet));

        // then
        assertThatThrownBy(() -> adminService.topUpBalance(topUpBalanceRequest))
                .isInstanceOf(TypesInError.class)
                .hasMessageContaining("Amount type is not valid");
    }
}