package uz.pdp.fastfood_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.fastfood_app.dto.*;
import uz.pdp.fastfood_app.entity.Food;
import uz.pdp.fastfood_app.entity.Restaurant;
import uz.pdp.fastfood_app.repo.FoodRepository;
import uz.pdp.fastfood_app.service.FoodService;
import uz.pdp.fastfood_app.service.JWTService;
import uz.pdp.fastfood_app.service.RestaurantService;
import uz.pdp.fastfood_app.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/discovery")
public class DiscoveryController {
    private final RestaurantService restaurantService;
    private final FoodService foodService;
@Autowired
private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private FoodRepository foodRepository;

    @GetMapping
    public ResponseEntity<String> discovery(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(foodRepository.findAll().toString());
    }

    private  String findUsername (String token){

        String email;
        if(token.startsWith("Bearer ")){
            email = token.substring(7);
            if (!email.equals("null")){
            String username= jwtService.extractUsername(email);
            if (userService.existUserByEmail(username)) {
            return username;
            }

            }

         return "User";
        }else {
            email="User";
        }
        return email;}

    public List<FoodResponseForDisco> foodResponseForDisco( ){
        List<FoodResponseForDisco> foodResponseForDiscoList = new ArrayList<>();
        List<Food> allFoods = foodRepository.findAllFoods();
        List<Long> id=new ArrayList<>();
        allFoods.forEach(food -> {
           id.add(food.getId());
        });

   return foodResponseForDiscoList; }
}