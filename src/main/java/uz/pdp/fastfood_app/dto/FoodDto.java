package uz.pdp.fastfood_app.dto;

import uz.pdp.fastfood_app.entity.enums.TypeEating;

public record FoodDto(Long id,String name, String photo, Double price, TypeEating typeEating) {
}
