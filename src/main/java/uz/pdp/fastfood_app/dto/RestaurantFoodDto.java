package uz.pdp.fastfood_app.dto;

import uz.pdp.fastfood_app.entity.Food;

import java.util.List;

public record RestaurantFoodDto (RestaurantDto restaurantDto,List<FoodDto> foodList){
}
