package edu.java.bot.service.userService;

import edu.java.bot.model.User;
import edu.java.bot.repository.userRepository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserService implements UserService {

    private final UserRepository repository;

    public SimpleUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void registration(long userId) {
        repository.addUser(userId);
    }

    @Override
    public User getUserById(long userId) {
        return repository.getUserById(userId);
    }

    @Override
    public List<User> getUsers() {
        return repository.getUsers();
    }

    @Override
    public void deleteUser(long userId) {
        repository.deleteUser(userId);
    }
}
