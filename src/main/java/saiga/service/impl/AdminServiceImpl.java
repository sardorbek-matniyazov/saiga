package saiga.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import saiga.model.Address;
import saiga.model.Cabinet;
import saiga.model.enums.AddressType;
import saiga.payload.MyResponse;
import saiga.payload.dto.CabinetDTO;
import saiga.payload.mapper.CabinetDTOMapper;
import saiga.payload.request.AddressRequest;
import saiga.payload.request.TopUpBalanceRequest;
import saiga.repository.AddressRepository;
import saiga.repository.CabinetRepository;
import saiga.service.AdminService;
import saiga.utils.exceptions.AlreadyExistsException;
import saiga.utils.exceptions.NotFoundException;
import saiga.utils.statics.MessageResourceHelperFunction;

import java.util.List;

import static saiga.payload.MyResponse._CREATED;
import static saiga.payload.MyResponse._UPDATED;
import static saiga.utils.statics.GlobalMethodsToHelp.parseStringMoneyToBigDecimalValue;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 24 Feb 2023
 **/
@Service
public record AdminServiceImpl(
        CabinetRepository cabinetRepository,
        AddressRepository addressRepository,
        CabinetDTOMapper cabinetDTOMapper,
        MessageResourceHelperFunction messageResourceHelper
) implements AdminService {
    @Override
    public List<CabinetDTO> getAllCabinets() {
        return cabinetRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                        .stream()
                        .map(cabinetDTOMapper)
                        .toList();
    }

    @Override
    public MyResponse createStaticAddress(AddressRequest addressRequest) {
        if (addressRepository.existsByTitleAndDistrictAndAddressType(addressRequest.title(), addressRequest.district(), AddressType.STATIC))
            throw new AlreadyExistsException(
                    String.format(
                            messageResourceHelper.apply("address.static.already_exists"),
                            addressRequest.title(),
                            addressRequest.district()
                    )
            );
        final Address save = addressRepository.save(
                new Address(
                        addressRequest.title(),
                        addressRequest.lat(),
                        addressRequest.lon(),
                        AddressType.STATIC,
                        addressRequest.district()
                )
        );
        return _CREATED()
                .setMessage(
                        messageResourceHelper.apply("address.static.created"))
                .addData("address", save);
    }

    @Override
    public List<Address> getAllStaticAddresses() {
        return addressRepository.findAllByAddressType(AddressType.STATIC);
    }

    @Override
    public MyResponse topUpBalance(TopUpBalanceRequest topUpBalanceRequest) {
        final Cabinet cabinet = cabinetRepository.findByUserId(topUpBalanceRequest.userID()).orElseThrow(
                () -> new NotFoundException(
                        String.format(
                                messageResourceHelper.apply("user.not_found_with_id"),
                                topUpBalanceRequest.userID()
                        )
                )
        );

        cabinet.setBalance(cabinet.getBalance().add(parseStringMoneyToBigDecimalValue(topUpBalanceRequest.amount())));

        return _UPDATED()
                .setMessage(
                        messageResourceHelper.apply("transfer_success")
                )
                .addData("data", cabinetDTOMapper.apply(cabinetRepository.save(cabinet)));
    }
}
