package uz.pdp.fastfood_app.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.fastfood_app.repo.UserRepository;
import uz.pdp.fastfood_app.service.JWTService;
import uz.pdp.fastfood_app.service.UserService;

// LogoutController.java

@RestController
@RequestMapping("/logout1")
@RequiredArgsConstructor
public class LogoutController {

private final UserService userService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping()
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        String username = findUsername(token);
//        userRepository.updateTokenByEmail(username,"null");
    return ResponseEntity.ok("You have been successful logout");
    }
    private  String findUsername (String token){

        String email="";
        if(token.startsWith("Bearer ")){
            email += token.substring(7);
        }
        return jwtService.extractUsername(email);}

}

