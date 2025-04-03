package uz.pdp.fastfood_app.dto;

import uz.pdp.fastfood_app.entity.enums.Role;

public record UserGetDataDto(String name, String email, String password, Role role, String address) {
}
