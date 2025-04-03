package uz.pdp.fastfood_app.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.fastfood_app.dto.UserGetCookieDto;
import uz.pdp.fastfood_app.dto.UserGetDataDto;
import uz.pdp.fastfood_app.dto.UserSignUpDto;
import uz.pdp.fastfood_app.entity.enums.Role;

import java.io.File;
@ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
@Builder
@Entity()
    @Table(name = "users")

    public class User{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String email;
        private String password;
        private Role role;
        private String address;
        @Column(columnDefinition = "TEXT")
        private String token;
        private String img;
        public User(UserSignUpDto userSignUpDto) {
            this.name= userSignUpDto.name();
            this.email= userSignUpDto.email();
            this.password= userSignUpDto.password();
            this.role=Role.CLIENT;
            this.address="";

        }
        public User(UserGetDataDto userGetDataDto) {
            this.name= userGetDataDto.name();
            this.email= userGetDataDto.email();
            this.password= userGetDataDto.password();
            this.role=userGetDataDto.role();
            this.address=userGetDataDto.address();
        }


    }


