package uz.pdp.fastfood_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.fastfood_app.dto.PaymentDto;
import uz.pdp.fastfood_app.dto.PaymentGetDto;
import uz.pdp.fastfood_app.dto.UserPaymentDto;
import uz.pdp.fastfood_app.entity.Payment;
import uz.pdp.fastfood_app.entity.User;
import uz.pdp.fastfood_app.entity.enums.PaymentType;
import uz.pdp.fastfood_app.repo.PaymentRepository;
import uz.pdp.fastfood_app.service.EmailService;
import uz.pdp.fastfood_app.service.JWTService;
import uz.pdp.fastfood_app.service.PaymentService;
import uz.pdp.fastfood_app.service.UserService;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;

@RequestMapping("/payment")

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private static String verifyDeleteCode,verifyAddCode;
    private final PaymentService paymentService;
    private final UserService userService;
    private static UserPaymentDto userPaymentDtoForDeleting;
    private static Payment payment;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private EmailService emailService;


    @GetMapping
    public ResponseEntity<PaymentGetDto> get(@RequestHeader("Authorization")String token) {
    return   paymentService.findPaymentByUserId(userService.findByEmail(findUsername(token)));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Payment> post(@PathVariable("id") String id,@RequestHeader("Authorization")String token) {
        return paymentService.findPaymentByIdAndUserId(Long.parseLong(id),userService.findByEmail(findUsername(token)));
    }


    @PutMapping("/add")
    public ResponseEntity<String> addPayment(@RequestHeader("Authorization") String token, @RequestParam String account_number, @RequestParam String payment_method, @RequestParam String payment_date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        LocalDate parsedDate;

        try {
            parsedDate = LocalDate.parse(payment_date + "-01", DateTimeFormatter.ofPattern("MM-yyyy-dd"));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use MM-yyyy.");
        }

        // Davom etish
        payment = new Payment(
                new PaymentDto(PaymentType.valueOf(payment_method), parsedDate, account_number),
                userService.findByEmail(findUsername(token))
        );

        return ResponseEntity.ok("You will have been sent a verification code to your email. Enter it via 'addVerification'.");
    }


    @PutMapping("/addVerification")
    public ResponseEntity<String> addVerification(@RequestHeader("Authorization")String token) {
        String username = findUsername(token);
        int i = new Random().nextInt(100000, 999999);
        verifyAddCode=String.valueOf(i);

        emailService.sendEmail(username,"Mr.Nobody",verifyAddCode);
        return ResponseEntity.ok("enter your verification code to " + username + " in way of '/addverification/{verifyCode}' ");
    }
    @PutMapping("/verify_added/{verifyCode}")
    public ResponseEntity<String> verifyAdded(@RequestHeader("Authorization")String token,@PathVariable("verifyCode")String code) {
        String username = findUsername(token);
        if (verifyAddCode.equals(code)) {
            System.err.println("oxshamidi");
            paymentService.addPayment(payment);
            return ResponseEntity.ok("You have been auth "+username);
        }else {
            return ResponseEntity.badRequest().body("Invalid verification code");
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePayment(@RequestHeader("Authorization") String token, @PathVariable String id){

        userPaymentDtoForDeleting=new UserPaymentDto(userService.findByEmail(findUsername(token)),Long.parseLong(id));
        return ResponseEntity.ok("You will have been sent verify code. Pls, Enter way of '/deleteVerification' !!!");

        //        return paymentService.deletePaymentByEmailAndId(userService.findByEmail(findUsername(token)),Long.parseLong(id));
    }
    @DeleteMapping("/deleteVerification")
    public ResponseEntity<String> deleteVerification(@RequestHeader("Authorization") String token){
      verifyDeleteCode=String.valueOf(new Random().nextInt(100000,999999));
        emailService.sendEmail(findUsername(token),"Mr.Nobody", verifyDeleteCode);
        return ResponseEntity.ok("You have been sent verification code ! enter");

    }
    @DeleteMapping("/deleteVerification/{verify}")
    public ResponseEntity<String> deleteVerification(@RequestHeader("Authorization") String token, @PathVariable String verify){
        if (String.valueOf(verifyDeleteCode).equals(verify)){
            verifyDeleteCode=null;
 payment=null;
            return paymentService.deletePaymentByUserIdAndId(userPaymentDtoForDeleting.user(), userPaymentDtoForDeleting.id());

        }else {
            return ResponseEntity.badRequest().body("You entered wrong verification code !");
        }

    }


    private  String findUsername (String token){

        String email=null;
        if(token.startsWith("Bearer ")){
            email = token.substring(7);
        }
        return jwtService.extractUsername(email);}

    @PostMapping("/update")
    public ResponseEntity<String> updatePayment(@RequestHeader("Authorization") String token, @RequestParam String account_number, @RequestParam String payment_method, @RequestParam String payment_date){
        return null;
    }
}
