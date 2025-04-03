package uz.pdp.fastfood_app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.fastfood_app.entity.Restaurant;
import uz.pdp.fastfood_app.repo.RestaurantRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantService {
private final RestaurantRepository restaurantRepository;
public List<Restaurant> findAllRestaurants(){
    return restaurantRepository.findAll();
}

public List<Restaurant> findAllRestaurantsById(List<Long> id) {
    return restaurantRepository.findAllById(id);
 }
 public List<String> findAllRestaurantsNamesByRestaurantId(List<Long> id) {
    return restaurantRepository.findRestaurantNamesById(id);
 }
 public Restaurant findRestaurantById(Long id) {
     Restaurant restaurant = restaurantRepository.findById(id).get();
     return
             restaurant;
 }
}
