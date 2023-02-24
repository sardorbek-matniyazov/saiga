package saiga.service.impl;

import org.springframework.stereotype.Service;
import saiga.model.Address;
import saiga.model.Cabinet;
import saiga.model.enums.AddressType;
import saiga.payload.MyResponse;
import saiga.payload.mapper.CabinetDTOMapper;
import saiga.payload.request.AddressRequest;
import saiga.payload.request.TopUpBalanceRequest;
import saiga.repository.AddressRepository;
import saiga.repository.CabinetRepository;
import saiga.service.AdminService;
import saiga.utils.exceptions.AlreadyExistsException;
import saiga.utils.exceptions.NotFoundException;

import static saiga.payload.MyResponse._CREATED;
import static saiga.payload.MyResponse._OK;
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
        CabinetDTOMapper cabinetDTOMapper
) implements AdminService {
    @Override
    public MyResponse getAllCabinets() {
        return _OK().addData(
                "data", cabinetRepository.findAll().stream().map(cabinetDTOMapper).toList()
        );
    }

    @Override
    public MyResponse createStaticAddress(AddressRequest addressRequest) {
        if (addressRepository.existsByTitleAndAddressType(addressRequest.title(), AddressType.STATIC))
            throw new AlreadyExistsException("This static address already exists");
        final Address save = addressRepository.save(
                new Address(
                        addressRequest.title(),
                        addressRequest.lat(),
                        addressRequest.lon(),
                        AddressType.STATIC
                )
        );
        return _CREATED()
                .setMessage("Static address created successfully")
                .addData("address", save);
    }

    @Override
    public MyResponse getAllStaticAddresses() {
        return _OK().addData("data", addressRepository.findAllByAddressType(AddressType.STATIC));
    }

    @Override
    public MyResponse topUpBalance(TopUpBalanceRequest topUpBalanceRequest) {
        final Cabinet cabinet = cabinetRepository.findByUserId(topUpBalanceRequest.userID()).orElseThrow(
                () -> new NotFoundException("User not fount with id " + topUpBalanceRequest.userID())
        );

        cabinet.setBalance(cabinet.getBalance().add(parseStringMoneyToBigDecimalValue(topUpBalanceRequest.amount())));

        return MyResponse._UPDATED()
                .setMessage("Transfer successfully")
                .addData("data", cabinetDTOMapper.apply(cabinetRepository.save(cabinet)));
    }
}
