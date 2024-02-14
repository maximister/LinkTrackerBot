package edu.java.bot.repository.userRepository;

import edu.java.bot.model.User;
import java.util.List;

public interface UserRepository {
    void addUser(long userId);

    User getUserById(long userId);

    List<User> getUsers();

    void deleteUser(long userId);
}
