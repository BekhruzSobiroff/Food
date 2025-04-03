package uz.pdp.fastfood_app.dto;

import uz.pdp.fastfood_app.entity.Restaurant;
import uz.pdp.fastfood_app.entity.enums.TypeEating;

import java.io.File;

public record FoodResponseForDisco(Long id, String name, TypeEating typeEating, RestaurantDto restaurantDto, File file) {
}
