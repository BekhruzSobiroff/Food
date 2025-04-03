package uz.pdp.fastfood_app.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.fastfood_app.entity.User;
import uz.pdp.fastfood_app.entity.UserPrincipal;
import uz.pdp.fastfood_app.repo.UserRepository;
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        System.err.println(user);
        if (user == null ) {
            logger.error("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(user);    }
}
