package uz.pdp.fastfood_app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.fastfood_app.entity.Favourite;
import uz.pdp.fastfood_app.entity.Food;
import uz.pdp.fastfood_app.entity.Restaurant;
import uz.pdp.fastfood_app.repo.FavouriteRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavouriteService {


    private final FavouriteRepository favouriteRepository;

    public List<Favourite> findAllFavourites(){
        return favouriteRepository.findAll();
    }
    @Transactional
    public List<Restaurant> getRestaurantsFavouritesById(Long id) {
        return favouriteRepository.findRestaurantByUserId(id);
    }

    @Transactional
    public List<Food> getFoodsFavouritesById(Long id) {
        return favouriteRepository.findFoodByUserId(id);
    }

    @Transactional
    public boolean addFoodToFavourites(Long userId, Long foodId) {
        return favouriteRepository.addFavourite1(foodId, userId);
    }

    @Transactional
    public boolean addRestaurantToFavourites(Long userId, Long restaurantId) {
        return favouriteRepository.addFavourite2(restaurantId, userId);
    }
}
