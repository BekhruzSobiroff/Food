package uz.pdp.fastfood_app.entity;

import java.io.File;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.fastfood_app.entity.enums.TypeEating;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "food")

public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private Double price;

    @Column(name = "type_eating")
    @Enumerated(EnumType.STRING)
    private TypeEating typeEating;


    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant_id_id")
    private Restaurant restaurantId;

    @Column(name = "name")
    private String name;

    @Column(name = "photo")
    private String photo;

}