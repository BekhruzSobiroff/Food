package uz.pdp.fastfood_app.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.fastfood_app.entity.Payment;
import uz.pdp.fastfood_app.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "SELECT COUNT(p) < 3 FROM Payment p WHERE p.user_id = :userId",nativeQuery = true)
    boolean isLessThanThreePayments(@Param("userId") Long userId);

    @Transactional
    boolean existsPaymentByAccountNum(String accountNum);

    Payment findPaymentByIdAndUser(Long id, User user);
@Transactional
    List<Payment> findPaymentByUser(User user);

    boolean existsPaymentByIdAndUser(Long id, User user);

    @Transactional
    @Query(value = "select * from payment p where p.id= :id and p.user_id =:user_id",nativeQuery = true)
    Payment findPaymentByIdAndUserId(@Param("id") Long id,@Param("user_id") Long user_Id);

    @Modifying
    @Transactional
    @Query(value = "insert into payment(account_num, amount, payment_date, payment_type, user_id) values (:account_num,:amount,:payment_date,:payment_type,:userId)",nativeQuery = true)
    void addPayment(@Param("payment_type") String PaymentName,@Param("payment_date") LocalDate paymentDate,@Param("amount") BigDecimal amount,@Param("account_num") String accountNum,@Param("userId") Long userId);
    @Transactional
    List<Payment> findPaymentByUserId(Long user_id);
@Query(value = "select true from payment p where p.user_id=:userId & p.id= :id",nativeQuery = true)
    boolean existsPaymentByUserIdAndId(@Param("userId") Long userId,@Param("id") Long id);



    boolean existsPaymentByUserId(Long userId);


}
