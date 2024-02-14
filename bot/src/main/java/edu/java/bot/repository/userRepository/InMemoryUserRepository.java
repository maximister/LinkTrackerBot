package edu.java.bot.repository.userRepository;

import edu.java.bot.model.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users;

    public InMemoryUserRepository() {
        users = new ArrayList<>();
    }

    @Override
    public void addUser(long userId) {
        if (getUserById(userId) == null) {
            users.add(new User(userId));
        }
    }

    @Override
    public User getUserById(long userId) {
        for (User user: users) {
            if (user.id() == userId) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public void deleteUser(long userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).id() == userId) {
                users.remove(i);
                break;
            }
        }
    }
}
