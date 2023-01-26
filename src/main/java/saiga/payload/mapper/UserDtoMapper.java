package saiga.payload.mapper;

import org.springframework.stereotype.Component;
import saiga.model.User;
import saiga.model.enums.Status;
import saiga.payload.dto.UserDTO;

import java.util.function.Function;

@Component
public class UserDtoMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getLang().toString(),
                user.getRole().getRole().toString(),
                user.getStatus()
        );
    }
}
