package uz.pdp.fastfood_app.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.fastfood_app.dto.PaymentDto;
import uz.pdp.fastfood_app.dto.UserAddressDto;
import uz.pdp.fastfood_app.dto.UserImgDto;
import uz.pdp.fastfood_app.dto.UserLoginDto;
import uz.pdp.fastfood_app.entity.Payment;
import uz.pdp.fastfood_app.entity.enums.PaymentType;
import uz.pdp.fastfood_app.service.*;

import java.util.Date;
import java.util.Random;

@RequiredArgsConstructor
@RestController
@RequestMapping("/enter")
public class EnterController {
    private final UserService userService;
    private final EmailService emailService;
    private static String mail;
    private static PaymentDto paymentDto;
    private static PaymentService paymentService;
    private static final int v_code= new Random().nextInt(100000,999999);
    private final MyUserDetailsService myUserDetailsService;
@Autowired
    private JWTService jwtService;
    @PostMapping("/address")
public ResponseEntity<String> enterAddress(@RequestHeader("Authorization")String token,@RequestParam String address) {
        boolean d = token.equals("d");

    if(address !=null) {
String findUser=findUsername(token);
        String email = jwtService.extractUsername(findUser);
        userService.updateAddressByEmail(new UserAddressDto(email,address));
        return ResponseEntity.ok("your address has been saved");
    }else{
        return ResponseEntity.ok("enter your address");
    }
    }
    @PostMapping("/payment")
    public ResponseEntity<String> enterPayment(@RequestParam String account_number, @RequestParam String payment_method, @RequestParam String payment_date) {
        paymentDto = new PaymentDto(PaymentType.valueOf(payment_method),null,account_number);
    return ResponseEntity.ok("enter email for verfication.The way is 'enter/verification'");
    }
     @PostMapping("/image")
    public ResponseEntity<String> enterImage(@RequestParam String image,@RequestHeader("Authorization") String token) {
        if(image !=null) {
           String email=jwtService.extractUsername(findUsername(token));
            userService.updateImgByEmail(new UserImgDto(email,image));
            return ResponseEntity.ok("your img has been saved");
        }else {
            return ResponseEntity.ok("enter your image");
        }

     }

@PostMapping("/verification")
    public ResponseEntity<String> verification(@RequestHeader("Authorization") String token) {
        String email= jwtService.extractUsername(token);
    String msg = "";
    if (userService.existUserByEmail(email)) {
        msg += "code is sent to "+email+" of your email and you enter the code in way of 'enter/verification'";

        if (paymentDto != null) {
            emailService.sendEmail(email, "verification", v_code + ", it is for payment verification");
            return ResponseEntity.ok(msg);
        } else {
            return ResponseEntity.badRequest().body("Please enter your detail");
        }
    } else {
        return ResponseEntity.badRequest().body("Please enter your email");
    }

}
@PostMapping("/code")
    public ResponseEntity<String> enterCode(@RequestParam String codes) {
        if(codes !=null) {
             if (paymentDto !=null) {
                if(comparing(String.valueOf(v_code)))   {
                    Long userId= userService.userIdWithEmail(mail);
                 //   paymentService.addPayment(new Payment(paymentDto,userId));
                  return ResponseEntity.ok("your payment has been saved");
                }else {
                    return ResponseEntity.badRequest().body("Please enter your email");
                }
            } else {
                return ResponseEntity.badRequest().body("Please enter your detail");
            }
        }else {
            return ResponseEntity.badRequest().body("Please enter correct your code");
        }
}

static boolean comparing(String code){
       return code.equals(String.valueOf(v_code));
}
private static String findUsername (String token){
 String email=null;
        if(token.startsWith("Bearer ")){
      email = token.substring(7);
 }
return email;}
}
