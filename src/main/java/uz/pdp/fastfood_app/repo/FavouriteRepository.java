package uz.pdp.fastfood_app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.fastfood_app.entity.Favourite;
import uz.pdp.fastfood_app.entity.Food;
import uz.pdp.fastfood_app.entity.Restaurant;

import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    @Modifying
    @Query("select f.restaurant from Favourite f where f.id= :userId")
    List<Restaurant> findRestaurantByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("select f.food from Favourite f where f.id= :userId")
    List<Food> findFoodByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "insert into Favourite(food_id, user_id) values (:food_id, :user_id)", nativeQuery = true)
    boolean addFavourite1(@Param("food_id") Long foodId, @Param("user_id") Long userId);

    @Modifying
    @Query(value = "insert into Favourite(restaurant_id, user_id) values (:restaurant_id, :user_id)", nativeQuery = true)
    boolean addFavourite2(@Param("restaurant_id") Long restaurantId, @Param("user_id") Long userId);

}
