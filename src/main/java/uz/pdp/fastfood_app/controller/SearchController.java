package uz.pdp.fastfood_app.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.fastfood_app.entity.Food;
import uz.pdp.fastfood_app.repo.FoodRepository;
import uz.pdp.fastfood_app.service.FoodService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController {
    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<List<Food>>search(@RequestParam String input) {
        List<Food> foodByInput = foodService.getFoodByInput(input);

        return ResponseEntity.ok( foodByInput);
}

}
