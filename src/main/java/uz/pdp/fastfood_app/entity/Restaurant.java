package uz.pdp.fastfood_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "restaurant")
public class Restaurant {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(name = "address")
 private String address;

 @Column(name = "name")
 private String name;

 @Column(name = "phone")
 private String phone;

 @Column(name = "photo")
 private String photo;

}
