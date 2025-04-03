package uz.pdp.fastfood_app.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.fastfood_app.dto.UserSignUpDto;
import uz.pdp.fastfood_app.entity.User;
import uz.pdp.fastfood_app.service.EmailService;
import uz.pdp.fastfood_app.service.UserService;

import java.util.Random;

@RestController
@RequiredArgsConstructor
public class SignUpController{
    private static UserSignUpDto signUpDto;
    private final UserService userService;
    private final EmailService emailService;

   private static int code= new Random().nextInt(100000,999999);
 private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
   @PostMapping("/signUp")
   public ResponseEntity<String> signUp(@RequestParam String name,@RequestParam String email, @RequestParam String password){

        signUpDto=new UserSignUpDto(name, email, encoder.encode(password));
   return ResponseEntity.ok("recode yo'liga o'ting");
   }
    @PostMapping("/signUpRecode")
public ResponseEntity<String> verification(@RequestParam String email){
        if (!userService.existUserByEmail(email)){

            emailService.sendEmail(email,"The Company",String.valueOf(code));
            return ResponseEntity.ok("emailingiziga kod jonatilindi /signUp/verification  yo'li orqali verifikatsiya kodlarini kiriting"+email);
        }else{
            return ResponseEntity.ok("ushbu email mavjud maslahat: parolni unutush bolimiga o'ting");
        }
    }
    @PostMapping("/verification")
    public ResponseEntity<String> recode(@RequestParam String verifyCode){
       if(verifyCode.equals(String.valueOf(code))){
           code=0;
              userService.add(new User(signUpDto));
              signUpDto=null;
           return ResponseEntity.ok("signUp successful");
       }else{
           return ResponseEntity.ok("your verify code is incorrect,pls, enter correct password");
       }
    }
}