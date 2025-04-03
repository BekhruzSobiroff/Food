package uz.pdp.fastfood_app.dto;

import uz.pdp.fastfood_app.entity.enums.PaymentType;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public record PaymentGetDto (String name, String email, List<PaymentDto> paymentDtoList){

}
