package uz.pdp.fastfood_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.fastfood_app.dto.FoodDto;
import uz.pdp.fastfood_app.dto.RestaurantDto;
import uz.pdp.fastfood_app.dto.RestaurantFoodDto;
import uz.pdp.fastfood_app.entity.Food;
import uz.pdp.fastfood_app.entity.Restaurant;
import uz.pdp.fastfood_app.entity.enums.TypeEating;
import uz.pdp.fastfood_app.service.FoodService;
import uz.pdp.fastfood_app.service.RestaurantService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantsController {
    private final RestaurantService restaurantService;
    private final FoodService foodService;

    @PostMapping("/{id}")
    public ResponseEntity<RestaurantFoodDto> addRestaurant(@PathVariable("id")String textId) {
       List<FoodDto> foodDtoList=new ArrayList<>();
        Long id = Long.valueOf(textId);
        List<Food> allFoodsByRestaurantId = foodService.findAllFoodsByRestaurantId(id);
        Restaurant restaurantById = restaurantService.findRestaurantById(id);
        allFoodsByRestaurantId.forEach(
                food -> {
                    foodDtoList.add(new FoodDto(food.getId(), food.getName(), food.getPhoto(), food.getPrice(),food.getTypeEating()));
                }
        );
   return ResponseEntity.ok(new RestaurantFoodDto(new RestaurantDto(restaurantById.getName(), restaurantById.getAddress(), new File(restaurantById.getPhoto()), restaurantById.getPhone()),foodDtoList));}

}
