package uz.pdp.fastfood_app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;
import uz.pdp.fastfood_app.entity.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Value

public class UserAddressDto implements Serializable {
    String email;
    String address;
}