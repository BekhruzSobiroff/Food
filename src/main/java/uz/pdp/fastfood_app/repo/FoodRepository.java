package uz.pdp.fastfood_app.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.fastfood_app.entity.Food;
import uz.pdp.fastfood_app.entity.enums.TypeEating;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    // Find all foods
    @Query("SELECT f FROM Food f")
    List<Food> findAllFoods();
@Query(value = "select * from food f where f.restaurant_id_id=:restaurant_id",nativeQuery = true)
List<Food> findAllByRestaurantId_id(@Param("restaurant_id") Long restaurantId);
    // Search foods by name
    @Query("SELECT f FROM Food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :input, '%'))")
    List<Food> searchFoodByName(@Param("input") String input);

    @Query(value = "SELECT f FROM Food f WHERE LOWER(f.typeEating) LIKE LOWER(CONCAT('%', :typeEating, '%'))")
    List<Food> searchFoodByTypeEating(@Param("typeEating")String typeEating);
boolean existsByName(String name);
    // Search foods by TypeEating
    @Query("SELECT f FROM Food f WHERE f.typeEating = :typeEating")
    List<Food> searchFoodByTypeEating(@Param("typeEating") TypeEating typeEating);

    // Find all foods by their type
    List<Food> findAllByTypeEating(@Param("typeEating") TypeEating typeEating);

    // Find foods by given IDs
    @Query("SELECT f FROM Food f WHERE f.id IN (:ids)")
    List<Food> findAllByIds(@Param("ids") List<Long> ids);

    // Find restaurant IDs by food IDs
    @Query(value = "SELECT f.restaurant_id_id FROM food f WHERE f.id IN :ids", nativeQuery = true)
    List<Long> findRestaurantIdsByIds(@Param("ids") List<Long> ids);

    // Count foods by TypeEating
    @Query("SELECT COUNT(f) FROM Food f WHERE f.typeEating = :typeEating")
    int countFoodByTypeEating(@Param("typeEating") TypeEating typeEating);

    @Modifying
    @Transactional
    @Query("UPDATE Food f SET f.photo = :photoId WHERE f.id = :foodId")
    void updatePhotoById(@Param("photoId") String photoId, @Param("foodId") Long foodId);

    @Query(value = "select f.restaurant_id_id from food f where id in:ids",nativeQuery = true )
    List<Long> findRestaurantIdsById(@Param("ids") List<Long> ids);
}
