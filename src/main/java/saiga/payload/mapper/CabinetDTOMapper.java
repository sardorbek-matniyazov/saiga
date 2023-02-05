package saiga.payload.mapper;

import org.springframework.stereotype.Service;
import saiga.model.Cabinet;
import saiga.payload.dto.CabinetDTO;

import java.util.function.Function;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 05 Feb 2023
 **/
@Service
public record CabinetDTOMapper(
        UserDTOMapper userDTOMapper
) implements Function<Cabinet, CabinetDTO> {
    @Override
    public CabinetDTO apply(Cabinet cabinet) {
        return new CabinetDTO(
                userDTOMapper.apply(cabinet.getUser()),
                cabinet.getBalance().toString()
        );
    }
}
