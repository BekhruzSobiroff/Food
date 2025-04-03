package uz.pdp.fastfood_app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.fastfood_app.dto.UserAddressDto;
import uz.pdp.fastfood_app.dto.UserImgDto;
import uz.pdp.fastfood_app.dto.UserLoginDto;
import uz.pdp.fastfood_app.entity.User;
import uz.pdp.fastfood_app.repo.UserRepository;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Transactional
    public  void updateNameByEmail(String email,String name){
        userRepository.updateNameByEmail(email,name);
    }


    private BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(12);

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public void add(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean existUserByEmailAndPassword(UserLoginDto userLoginDto) {
        return userRepository.existsUserByEmailAndPassword(userLoginDto.email(), userLoginDto.password());
    }

    public boolean existUserByEmail(String email) {
        return userRepository.existsUsersByEmail(email);
    }

    @Transactional
    public void updatePasswordByEmail(UserLoginDto userLoginDto) {
        userRepository.update_PasswordByEmail(userLoginDto.email(), bCryptPasswordEncoder.encode(userLoginDto.password()));
    }

    @Transactional
    public void updateAddressByEmail(UserAddressDto userAddressDto) {
        userRepository.update_AddressByEmail(userAddressDto.getEmail(), userAddressDto.getAddress());
    }

    @Transactional
    public Long userIdWithEmail(String email) {
        return userRepository.findIdByEmail(email);
    }

    @Transactional
    public void updateImgByEmail(UserImgDto userImgDto) {
        userRepository.update_ImgByEmail(userImgDto.getEmail(),new File(userImgDto.getImg()));
    }

    @Transactional
    public ResponseEntity<String> verify(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        String token = jwtService.generateToken(email, userRepository.findRoleByEmail(email));
        userRepository.updateTokenByEmail(email, token);
        return ResponseEntity.ok(token);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateTokenByEmail(String email, String aNull) {
    userRepository.updateTokenByEmail(email, aNull);
    }
}
