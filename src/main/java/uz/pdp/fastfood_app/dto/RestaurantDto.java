package uz.pdp.fastfood_app.dto;

import java.io.File;

public record RestaurantDto (String name, String address, File photo,String phoneNumber){
}
