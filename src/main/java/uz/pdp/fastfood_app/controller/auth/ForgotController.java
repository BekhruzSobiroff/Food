package uz.pdp.fastfood_app.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.fastfood_app.dto.UserLoginDto;
import uz.pdp.fastfood_app.service.EmailService;
import uz.pdp.fastfood_app.service.UserService;

import java.util.Random;

@RestController
@RequiredArgsConstructor
public class ForgotController {
    private final UserService userService;
    private final EmailService emailService;
    private static final int code= new Random().nextInt(100000,999999);
    private static String mail;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @PostMapping("/forgot")
    public ResponseEntity<String> forgot(@RequestParam String email) {
    if(userService.existUserByEmail(email)){
        mail = email;
        emailService.sendEmail(email,"Mr.Nobody",String.valueOf(code));
        return ResponseEntity.ok().body(mail+" has been taken verification code");
    }else {
        return ResponseEntity.ok("There is no that user.Thus Please, sign up");
    }
}
@PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String verifyCode) {
        if(verifyCode.equals(String.valueOf(code))){
            return ResponseEntity.ok().body("You enter way of /forgot/recode and you write your modifying password");
        }else {
            return ResponseEntity.ok().body("Invalid verification code,pls try again");
        }
}
@PostMapping("/recode")
    public ResponseEntity<String> recode(@RequestParam String password_1, @RequestParam String password_2) {
        if(password_1.equals(password_2)){
            userService.updatePasswordByEmail(new UserLoginDto(mail, password_2));
return ResponseEntity.ok().body("You have successfully verified your password");
        }else {
            return ResponseEntity.ok().body("Passwords do not match");
        }}

}
