package uz.pdp.fastfood_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.fastfood_app.entity.Payment;
import uz.pdp.fastfood_app.entity.User;
import uz.pdp.fastfood_app.repo.UserRepository;
import uz.pdp.fastfood_app.service.JWTService;
import uz.pdp.fastfood_app.service.PaymentService;
import uz.pdp.fastfood_app.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
   private JWTService jwtService;
    @Autowired
    private PaymentService paymentService;

    public ProfileController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;

    }
    @GetMapping("/payment")
    public ResponseEntity<List<Payment>> payment(@RequestHeader("Authorization")String token) {
        return paymentService.paymentResponseEntity(userService.findByEmail(findUsername(token)));
    }

    @PostMapping
    public ResponseEntity<String> addProfile(@RequestHeader("Authorization")String token) {
        User byEmail = userService.findByEmail(findUsername(token));
    return ResponseEntity.ok(byEmail.getName()+",\n"+byEmail.getEmail()+",\n"+byEmail.getRole());
    }

    @PostMapping("/becomeDelivery")
    public ResponseEntity<String> becomeDelivery(@RequestHeader("Authorization")String token) {
        String email = findUsername(token);
        userRepository.updateRolesToDeliveryByEmail(email);
return ResponseEntity.ok("successfully");
    }
    @PostMapping("/becomeClient")
    public ResponseEntity<String> becomeClient(@RequestHeader("Authorization")String token) {
        String email = findUsername(token);
        userRepository.updateRolesToClientByEmail(email);
        return ResponseEntity.ok("successfully");
    }
    private  String findUsername (String token){

        String email=null;
        if(token.startsWith("Bearer ")){
            email = token.substring(7);
        }
        return jwtService.extractUsername(email);
    }
    @PostMapping("/updateName")
    public ResponseEntity<String> updateName(@RequestHeader("Authorization")String token,String name) {
        String email = findUsername(token);
        userService.updateNameByEmail(email,name);
        return ResponseEntity.ok("successfully");
    }

}





