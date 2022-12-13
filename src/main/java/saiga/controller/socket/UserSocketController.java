package saiga.controller.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import saiga.payload.dto.SearchWithField;
import saiga.model.User;
import saiga.repository.UserRepository;

import java.util.List;

@Controller
public class UserSocketController {
    private final UserRepository repository;

    @Autowired
    public UserSocketController(UserRepository repository) {
        this.repository = repository;
    }

    @MessageMapping("/search")
    @SendTo("/socket/users")
    public List<User> greeting(SearchWithField message) throws Exception {
        System.out.println("message = " + message.getName());
        Thread.sleep(1000);
        List<User> allByFirstName = repository.findAllByFirstNameLike(message.getName());
        System.out.println(allByFirstName);
        return allByFirstName;
    }


}
