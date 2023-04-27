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
import saiga.service.telegram.TgMainService;
import saiga.utils.exceptions.AlreadyExistsException;
import saiga.utils.exceptions.NotFoundException;
import saiga.utils.statics.GlobalMethodsToHelp;
import saiga.utils.statics.MessageResourceHelperFunction;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.List;

import static saiga.payload.MyResponse.*;

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
        MessageResourceHelperFunction messageResourceHelper,
        GlobalMethodsToHelp globalMethodsToHelp,
        TgMainService tgMainService
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

        // check amount is valid
        globalMethodsToHelp.isValidDecimalValue(topUpBalanceRequest.amount());

        cabinet.setBalance(cabinet.getBalance().add(new BigDecimal(topUpBalanceRequest.amount())));

        return _UPDATED()
                .setMessage(
                        messageResourceHelper.apply("transfer_success")
                )
                .addData("data", cabinetDTOMapper.apply(cabinetRepository.save(cabinet)));
    }

    @Override
    public MyResponse backupDb() {
        // Define the database connection parameters
        String username = "saiga";
        String password = "this_is_your_password";
        String database = "saiga";
        String host = "localhost";
        String port = "5432"; // Change this to the port number of your database

        // Define the command to execute the database dump
        String[] command = {"pg_dump", "-U", username, "-h", host, "-p", port, "-F", "c", "-b", "-v", "-f", "database_dump.backup", database};

        // Execute the command and save the output to a file
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Provide the password to the process
        OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
        try {
            writer.write(password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Check if the command executed successfully
        if (exitCode == 0) {
            _OK().setMessage("Database dump completed successfully");
        } else {
            _BAD_REQUEST().setMessage("Database dump failed");
        }

        // sent file to tg
        tgMainService.sendFile("database_dump.backup");
        return _OK().setMessage("Database dump completed successfully");
    }
}
