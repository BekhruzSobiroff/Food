package uz.pdp.fastfood_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.fastfood_app.dto.PaymentDto;
import uz.pdp.fastfood_app.dto.PaymentGetDto;
import uz.pdp.fastfood_app.entity.Payment;
import uz.pdp.fastfood_app.entity.User;
import uz.pdp.fastfood_app.repo.PaymentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public ResponseEntity<Payment> findPaymentByIdAndUserId(Long id, User userId) {
        return ResponseEntity.ok(paymentRepository.findPaymentByIdAndUserId(id, userId.getId()));
    }

    public void addPayment(Payment payment) {
        Long userId = payment.getUser().getId();
        System.out.println(userId);// Agar `payment` da `getUser()` mavjud bo'lsa
        System.err.println("payment 11= " + payment);
        if (!paymentRepository.existsPaymentByAccountNum(payment.getAccountNum())) {
            System.err.println("payment12 = " + payment);
            System.out.println(payment);
paymentRepository.addPayment(payment.getPaymentType().name(),payment.getPaymentDate(),payment.getAmount(),payment.getAccountNum(),payment.getUser().getId());        }
    }

    public ResponseEntity<PaymentGetDto> findPaymentByUserId(User userId) {
        List<PaymentDto> paymentDtoList=new ArrayList<>();
    AtomicReference<PaymentGetDto> paymentGetDto = new AtomicReference<>();
        List<Payment> paymentByUserId = paymentRepository.findPaymentByUserId(userId.getId());
    paymentByUserId.forEach(payment ->{
        paymentDtoList.add(new PaymentDto(payment.getPaymentType(),payment.getPaymentDate(),payment.getAccountNum()));
        PaymentGetDto paymentGetDto1 = new PaymentGetDto(payment.getUser().getName(), payment.getUser().getEmail(), paymentDtoList);
   paymentGetDto.set( paymentGetDto1);
    });
   return ResponseEntity.ok(paymentGetDto.get());
    }

    public ResponseEntity<String> deletePaymentByUserIdAndId(User userId, Long id) {
        if (paymentRepository.existsPaymentByUserIdAndId(userId.getId(), id)) {
            paymentRepository.delete(paymentRepository.findPaymentByIdAndUserId(id, userId.getId()));
            return ResponseEntity.ok("Successfully deleted your payment");
        } else {
            return ResponseEntity.badRequest().body("You have not got that payment.");
        }
    }
    public ResponseEntity<List<Payment>> paymentResponseEntity(User user){
        return ResponseEntity.ok(paymentRepository.findPaymentByUser(user));
    }
}
