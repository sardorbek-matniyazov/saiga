package saiga.payload.mapper;

import org.springframework.stereotype.Service;
import saiga.model.User;
import saiga.payload.dto.UserDTO;

import java.util.function.Function;

@Service
public record UserDTOMapper() implements Function<User, UserDTO> {
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
