package uz.pdp.fastfood_app.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.lang.Long;
import java.lang.reflect.Type;
import java.util.List;

import lombok.RequiredArgsConstructor;
import uz.pdp.fastfood_app.dto.FoodCountDto;
import uz.pdp.fastfood_app.dto.ResponseCategoriesDto;
import uz.pdp.fastfood_app.entity.Food;
import uz.pdp.fastfood_app.entity.enums.TypeEating;
import uz.pdp.fastfood_app.repo.FoodRepository;
import uz.pdp.fastfood_app.service.FoodService;
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class CategoryController {
    private final FoodService foodService;

    @GetMapping
    public ResponseEntity<List<Food>> products(){
    return ResponseEntity.ok(foodService.getAlls());
    }
        @GetMapping("/categories")
        public ResponseEntity<String> categories(){
            return ResponseEntity.ok(responseForCategory());
        }

    @GetMapping ("/{id}")
    public ResponseEntity<Food> ids(@PathVariable("id")String idString){
    Long   id=Long.valueOf(idString);
    return foodService.giveProduct(id);
    }
    @GetMapping("/categories/{id}")
    public ResponseEntity<String> categories(@PathVariable("id")String idString){
     Long id=Long.valueOf(idString);
    return foodService.giveCategories(id);
    }
    public String responseForCategory(){

    StringBuilder result= new StringBuilder();
        TypeEating[] typeEatingS=TypeEating.values();
    for (TypeEating typeEating:typeEatingS) {
        result.append(typeEating.name()).append(" :").append(foodService.countByTypeEating(typeEating)).append("\n");
    }
     return result.toString(); }
}
