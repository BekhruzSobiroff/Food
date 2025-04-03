package uz.pdp.fastfood_app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import uz.pdp.fastfood_app.dto.FoodPhotoDto;
import uz.pdp.fastfood_app.dto.FoodResponseForDisco;
import uz.pdp.fastfood_app.dto.ResponseCategoriesDto;
import uz.pdp.fastfood_app.dto.RestaurantDto;
import uz.pdp.fastfood_app.entity.Food;
import uz.pdp.fastfood_app.entity.enums.TypeEating;
import uz.pdp.fastfood_app.repo.FoodRepository;


/**
 * FoodService class is responsible for managing food-related operations.
 * It provides methods to retrieve foods based on their type and search for foods by input.
 */

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;

    private Optional<Food> byId;

    /**
     * Retrieves all foods of type FOOD.
     *
     * @return a list of foods that have the type FOOD.
     */
    public List<Food> getFoodTypeOfFood() {
        List<Food> foods = foodRepository.findAllByTypeEating(TypeEating.FOOD);
        System.out.println("Found foods: " + foods); // Add this line for debugging
        return foods;
    }
public List<Food> findAllFoodsByRestaurantId(Long restaurantId) {
        return foodRepository.findAllByRestaurantId_id(restaurantId);
}

    /**
     * Retrieves all foods of type DESERT.
     *
     * @return a list of foods that have the type DESERT.
     */
    @Transactional
    public List<Food> getFoodTypeOfDesert() {

        return foodRepository.findAllByTypeEating(TypeEating.DESSERT);
    }

    /**
     * Retrieves all foods of type DRINKS.
     *
     * @return a list of foods that have the type DRINKS.
     */
@Transactional
    public List<Food> getFoodTypeOfDrinks() {
        return foodRepository.findAllByTypeEating(TypeEating.DRINKS);
    }






    public List<Food> getAlls() {
      return foodRepository.findAllFoods();

    }

    public List<Food> getAllFoodById(List<Long> ids) {
        return foodRepository.findAllByIds(ids);
    }


    public int countByTypeEating(TypeEating typeEating) {
        System.out.println("salom");
        return foodRepository.countFoodByTypeEating(typeEating);
}



public ResponseEntity<String> giveCategories(Long id){

        TypeEating[] typeEatingForId = TypeEating.values();
TypeEating typeEating=typeEatingForId[id.intValue()];
    int n=foodRepository.countFoodByTypeEating(typeEating);
int y=id.intValue();
    List<Food> allByTypeEating = foodRepository.findAllByTypeEating(typeEating);
    return ResponseEntity.ok(allByTypeEating.toString());
}
public ResponseEntity<Food> giveProduct(Long id){
return ResponseEntity.ok(foodRepository.findById(id).get());    
}


    public List<Food> getFoodByInput(String input) {
        if (!foodRepository.existsByName(input)) {
            return foodRepository.searchFoodByTypeEating(input);
        }else return foodRepository.searchFoodByName(input);
}
}
