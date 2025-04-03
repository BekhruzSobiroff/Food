package uz.pdp.fastfood_app.controller.auth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.fastfood_app.service.UserService;

@RestController
@RequestMapping("/login1")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;



    

    @PostMapping
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password, HttpSession httpSession) {

        return userService.verify(email, password);
    }
}
