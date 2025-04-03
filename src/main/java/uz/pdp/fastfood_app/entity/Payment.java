package uz.pdp.fastfood_app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import uz.pdp.fastfood_app.dto.PaymentDto;
import uz.pdp.fastfood_app.entity.enums.PaymentType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
@ToString
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type",nullable = false)
    private PaymentType paymentType;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "account_num", length = 16)
    private String accountNum;

    public Payment(PaymentDto paymentDto, User user) {
        this.paymentType = paymentDto.paymentType();
        this.accountNum = paymentDto.accountNum();
        this.paymentDate = paymentDto.paymentDate();
        this.amount = new BigDecimal("100000000");
        this.user=user;// Use BigDecimal for
    }
}