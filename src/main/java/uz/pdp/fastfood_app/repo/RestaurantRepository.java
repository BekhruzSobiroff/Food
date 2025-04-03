package uz.pdp.fastfood_app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.fastfood_app.entity.Restaurant;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Modifying
    @Query("select r.name from Restaurant r where r.id in :idList")
    List<String> findRestaurantNamesById(@Param("idList") List<Long> idList);

}