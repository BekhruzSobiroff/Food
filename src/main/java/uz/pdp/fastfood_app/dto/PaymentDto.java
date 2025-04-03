package uz.pdp.fastfood_app.dto;

import uz.pdp.fastfood_app.entity.Payment;
import uz.pdp.fastfood_app.entity.enums.PaymentType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Date;

/**
 * DTO for {@link Payment}
 */
public record PaymentDto(PaymentType paymentType, LocalDate paymentDate, String accountNum) implements Serializable {
}