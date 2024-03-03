package edu.java.bot.service.userService;

import edu.java.bot.model.User;
import java.util.List;

public interface UserService {
    void registration(long userId); //add user

    User getUserById(long userId);

    List<User> getUsers();

    void deleteUser(long userId);
}
