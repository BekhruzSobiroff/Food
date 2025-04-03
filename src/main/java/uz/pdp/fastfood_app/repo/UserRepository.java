package uz.pdp.fastfood_app.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.fastfood_app.entity.User;

import java.io.File;

@Repository  // Bu annotatsiyani qo'shing
public interface UserRepository extends JpaRepository<User, Long> {

    User findAllById(Long id);
@Transactional
    @Query("select u.id from User u where u.email = :email")
    Long findIdByEmail(@Param("email") String email);
@Transactional
    boolean existsUserByEmailAndPassword(String email, String password);
@Transactional
    boolean existsUsersByEmail(String email);

    @Modifying
    @Query("update User u set u.password = :password where u.email = :email")
    void update_PasswordByEmail(@Param("email") String email, @Param("password") String newPassword);

    @Modifying
    @Query("update User u set u.address = :address where u.email = :email")
    void update_AddressByEmail(@Param("email") String email, @Param("address") String address);

    @Modifying
    @Query("update User u set u.img = :img where u.email = :email")
    void update_ImgByEmail(@Param("email") String email, @Param("img") File img);

    @Query("select u from User u where u.email = :email")
     User findByEmails(@Param("email") String email);
@Transactional
    User findByEmail(String mail);

    @Query("select u.password from User u where u.email =:email")
    String findPasswordByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.token = :token WHERE u.email = :email")
    void updateTokenByEmail(@Param("email") String email, @Param("token") String token);



    @Query("select u.role from User u where u.email= :email")
    String findRoleByEmail(String email);

@Transactional
    @Query(value = "select u.address from users u where u.email= :email",nativeQuery = true)
    String findAddressByEmail(String email);

@Transactional
@Modifying
@Query(value = "update users u set role=1 where u.email =:email",nativeQuery = true)
    void updateRolesToDeliveryByEmail(@Param("email") String email);
    @Transactional
    @Modifying
    @Query(value = "update users u set role=0 where u.email =:email",nativeQuery = true)
    void updateRolesToClientByEmail(@Param("email") String email);

    @Modifying
    @Query("update User u set u.name=:newName where u.email=:email")
    void updateNameByEmail(@Param("email") String email,@Param("newName") String newName);

@Query("select true from User u where u.email=:email and u.token=:token")
    boolean existsTokenByEmail(@Param("email") String email,@Param("token") String token);
}
