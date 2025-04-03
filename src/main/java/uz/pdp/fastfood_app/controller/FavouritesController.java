package uz.pdp.fastfood_app.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.fastfood_app.entity.Food;
import uz.pdp.fastfood_app.entity.Restaurant;
import uz.pdp.fastfood_app.service.FavouriteService;
import uz.pdp.fastfood_app.service.JWTService;
import uz.pdp.fastfood_app.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/favourite")
public class FavouritesController {

    private final FavouriteService favouriteService;
    private final UserService userService;
    @Autowired
    private  JWTService jwtService;

    @GetMapping
    public ResponseEntity<String> stringResponseEntity(@RequestHeader("Authorization") String token ){
        String email = findUsername(token);

        System.out.println();
        return ResponseEntity.ok(favouriteService.getFoodsFavouritesById(userService.userIdWithEmail(email)).toString()); }
    @PostMapping("/food")
    public ResponseEntity<List<Food>> foodResponseEntity(@RequestHeader("Authorization") String token) {
        String email = findUsername(token);
        return ResponseEntity.ok(favouriteService.getFoodsFavouritesById(userService.userIdWithEmail(email)));
    }

    @PostMapping("/restaurant")
    public ResponseEntity<List<Restaurant>> restaurantResponseEntity(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(favouriteService.getRestaurantsFavouritesById(userService.userIdWithEmail(findUsername(token))));
    }

    @PutMapping("/food/add")
    public ResponseEntity<String> addFoodResponseEntity(@RequestParam String id,  @RequestHeader("Authorization") String token) {
        favouriteService.addFoodToFavourites(userService.userIdWithEmail(findUsername(token)),Long.parseLong(id));

        return ResponseEntity.ok("Food added successfully");
    }

    @PutMapping("/restaurant/add")
    public ResponseEntity<String> addRestaurantResponseEntity(@RequestParam String id, @RequestHeader("Authorization") String token) {
        favouriteService.addRestaurantToFavourites(userService.userIdWithEmail(findUsername(token)), Long.parseLong(id));
        return ResponseEntity.ok("Restaurant added successfully");
    }
    private  String findUsername (String token){

        String email=null;
        if(token.startsWith("Bearer ")){
            email = token.substring(7);
        }
        return jwtService.extractUsername(email);}
}

