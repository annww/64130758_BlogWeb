package ntu.hongdta_64130758.services.implement;

import ntu.hongdta_64130758.models.User;
import ntu.hongdta_64130758.repositories.UserRepository;
import ntu.hongdta_64130758.security.CustomUserDetails;
import ntu.hongdta_64130758.services.interf.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
