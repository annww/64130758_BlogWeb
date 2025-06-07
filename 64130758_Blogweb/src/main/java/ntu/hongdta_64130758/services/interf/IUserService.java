package ntu.hongdta_64130758.services.interf;

import java.util.List;
import ntu.hongdta_64130758.models.User;

public interface IUserService {
    User findByUsername(String username);
    List<User> getAllUsers();
    User findById(Long id);
    User saveUser(User user);
    void deleteUserById(Long id);
}
