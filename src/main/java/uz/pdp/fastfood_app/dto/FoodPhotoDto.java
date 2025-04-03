package uz.pdp.fastfood_app.dto;

import uz.pdp.fastfood_app.entity.Food;

import java.io.File;

public record FoodPhotoDto(Long id,Food food, File photo) {
}
