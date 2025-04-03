package uz.pdp.fastfood_app.dto;

import java.util.List;

public record ResponseCategoriesDto(String name, int count, List<FoodResponseForDisco> foodResponseForDiscoList) {
}
