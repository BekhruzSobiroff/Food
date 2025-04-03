package uz.pdp.fastfood_app.dto;

import lombok.Value;
import uz.pdp.fastfood_app.entity.User;

/**
 * DTO for {@link User}
 */
@Value
public class UserImgDto {
 String email;
 String img;
}
