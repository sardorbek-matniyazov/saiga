package saiga.controller.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import saiga.payload.request.SearchWithUsersFieldRequest;
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
    public List<User> greeting(SearchWithUsersFieldRequest message) throws Exception {
        System.out.println("message = " + message.name());
        Thread.sleep(1000);
        List<User> allByFirstName = repository.findAllByFirstNameLike(message.name());
        System.out.println(allByFirstName);
        return allByFirstName;
    }


}
