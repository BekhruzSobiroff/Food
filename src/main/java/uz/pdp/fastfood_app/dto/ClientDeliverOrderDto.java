package uz.pdp.fastfood_app.dto;

import lombok.*;
import uz.pdp.fastfood_app.entity.User;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ClientDeliverOrderDto{
       private String clientEmail;
       private String deliveryEmail;
       private List<FoodCountDto> foodCountDtoList;
       private Double price;
       private boolean isArrived; private boolean result;
       private String info;
}
