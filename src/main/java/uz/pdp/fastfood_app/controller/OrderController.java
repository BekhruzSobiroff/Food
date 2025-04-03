package uz.pdp.fastfood_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.fastfood_app.dto.ClientDeliverOrderDto;
import uz.pdp.fastfood_app.dto.FoodCountDto;
import uz.pdp.fastfood_app.entity.User;
import uz.pdp.fastfood_app.entity.enums.Role;
import uz.pdp.fastfood_app.repo.FoodRepository;
import uz.pdp.fastfood_app.repo.PaymentRepository;
import uz.pdp.fastfood_app.repo.RestaurantRepository;
import uz.pdp.fastfood_app.repo.UserRepository;
import uz.pdp.fastfood_app.service.EmailService;
import uz.pdp.fastfood_app.service.FoodService;
import uz.pdp.fastfood_app.service.JWTService;


import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")

public class OrderController {

    private final FoodService foodService;
    private static boolean isOrder,result,isArriveToRestaurant;
    private static String userEmail, deliveryEmail,info;
    private static List<FoodCountDto> foodCountDtoListForProcess=new ArrayList<>();
    private static List<ClientDeliverOrderDto> clientDeliverOrderDtoList=new ArrayList<>();
    private static int verifyCode=0;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;



    static boolean containsDuplicates(List<Long> list) {
       Set<Long> set=new HashSet<>(list);
      return set.size() < list.size();

    }

    @PostMapping
    public String addOrder(@RequestHeader("Authorization")String token){
        String email = findUsername(token);

  if( userRepository.findRoleByEmail(email).equals(Role.CLIENT.name())){
        return foodService.getAlls().toString()+" agar bitta yoki yoki kop mahsulotlarni tanalasangiz id isini yozing";
    }else{
      return foodService.getAlls().toString()+" agar bitta yoki yoki kop mahsulotlarni tanalasangiz id isini yozing yoki byurtmani qabul qiling";

  }
    }


@PostMapping("/addExact")
    public ResponseEntity<String> addOrder(@RequestBody List<FoodCountDto> foodCountDtoList, @RequestHeader("Authorization")String token) {
if (paymentRepository.existsPaymentByUserId(userRepository.findIdByEmail(findUsername(token)))){
        List <Long> ids=new ArrayList<>();
        for (FoodCountDto foodCountDto:foodCountDtoList){
   ids.add(foodCountDto.id());
}
        if (containsDuplicates(ids)){
    Map<Long, Integer> elementCount = new HashMap<>();

    for (Long item : ids) {
        elementCount.put(item, elementCount.getOrDefault(item, 0) + 1);
    }

    // Takrorlangan elementlarni chiqaramiz
    for (Map.Entry<Long, Integer> entry : elementCount.entrySet()) {
        if (entry.getValue() > 1) {
            return ResponseEntity.ok(entry.getKey() + " obyekti " + entry.getValue() + "ta bo‘lib qoldi");
        }
    }
        }
 if(!isClientOrdered(findUsername(token))) {


    foodCountDtoListForProcess.addAll(foodCountDtoList);
    userEmail = findUsername(token);
     ClientDeliverOrderDto clientDeliverOrderDto = new ClientDeliverOrderDto(userEmail, "null", foodCountDtoList, 0d, false, false, "");
clientDeliverOrderDtoList.add(clientDeliverOrderDto);
     return ResponseEntity.ok("successfully and also you can get info about your order in way of 'order/sendCode'");
}else{
return ResponseEntity.ok("you have got order");
}}else return ResponseEntity.badRequest().body("you have not got payment");
}
@PostMapping("/sendCode")
public ResponseEntity<String> sendCode(@RequestHeader("Authorization")String token){
        String email = findUsername(token);
    if (isClientOrdered(email)) {
        verifyCode=new Random().nextInt(100_000,999_999);
        emailService.sendEmail(email,"Mr.Nobody",String.valueOf(verifyCode));
        return ResponseEntity.ok("order/verify for verification ");
    }else {

        return ResponseEntity.badRequest().body("Bro, You must add order for using the proccess");
    }

}
@PostMapping("/verifyA{verifyCode}")
public ResponseEntity<String> verify(@RequestHeader("Authorization")String token,@PathVariable("verifyCode")String code){
       String msg="";
        String email = findUsername(token);
        if (isClientOrdered(email)&&String.valueOf(verifyCode).equals(code)){
            if (!userRepository.findAddressByEmail(email).isEmpty()){
            doEmptyDeliveryName(email);
                msg+="loaded sucessfully";
            }else {
                msg+="pls enter your address";
            }
        }else{
            msg+="wrong verify code";
        }
return ResponseEntity.ok(msg);}
    @PostMapping("/get_order")
    public ResponseEntity<String> getOrder(@RequestHeader("Authorization")String token){

       String mail = findUsername(token);
if(paymentRepository.existsPaymentByUserId(userRepository.findIdByEmail(mail))) {
    if (userRepository.findRoleByEmail(mail).equals(Role.DELIVERY.name())) {
        if (isClientOrdered(mail)) {
            return ResponseEntity.badRequest().body("you can not take your order");

        } else {
            return ResponseEntity.ok(clientDeliverOrderDtos().toString());
        }
    } else {
        return ResponseEntity.badRequest().body("you have got order");
    }
}else {
    return ResponseEntity.badRequest().body("you hav not got payment");
}
    }
@PostMapping("/get_order/{clientEmail}")
    public ResponseEntity<String> getOrder( @RequestHeader("Authorization")String token,@PathVariable("clientEmail") String clientEmail) {
    String mail = findUsername(token);
    if (userRepository.findRoleByEmail(mail).equals(Role.DELIVERY.name())&&!mail.equals(clientEmail)){
        if (isClientOrdered(mail)) {
            return ResponseEntity.badRequest().body("you can not take your order");

        } else {
          if (paymentRepository.existsPaymentByUserId(userRepository.findIdByEmail(mail))){
              return ResponseEntity.badRequest().body("if you have not got your payment, Then ,you can not use it");
          }
           if (userRepository.existsUsersByEmail(mail)&&isClientOrdered(clientEmail)&&isTookOrder(clientEmail)){
               clientDeliverOrderDtoList.forEach(clientDeliverOrderDto->{
                   if (clientDeliverOrderDto.getClientEmail().equals(clientEmail)) {

                       clientDeliverOrderDto.setDeliveryEmail(mail);
                   }
               } );

                List<FoodCountDto> foodCountDtoList = foodCountDto(mail);
               List<Long > ids=new ArrayList<>();
               foodCountDtoList.forEach(
                       foodCountDto ->
                       {
                          ids.add(foodCountDto.id());
                       }
               );
               List<Long> restaurantIdsById = foodRepository.findRestaurantIdsById(ids);
               Set<Long> id=new HashSet<>(restaurantIdsById);
               restaurantIdsById=new ArrayList<>(id);
               List<String> restaurantNamesById = restaurantRepository.findRestaurantNamesById(restaurantIdsById);


               return ResponseEntity.ok("sizga buyurtma boldi \n"+foodCountDtoList+"  va siz  "+ restaurantNamesById+" ga boring");

            } else {
                return ResponseEntity.ok("sizga hali buyurtma mavjud emas");
            }
        }
    }else return ResponseEntity.ok("unacceptable role");
}
@PostMapping("/isRestaurant")
public ResponseEntity<String> isRestaurant(@RequestHeader("Authorization")String token) {
        String username=findUsername(token);
        if (isDeliverOrdered(username)){
            updateIsArrived(username,true);
            return ResponseEntity.ok("successfully added");
        } else if (isClientOrdered(username)) {
            if (isArrivedToRestaurant(username)){
                return ResponseEntity.ok("Delivery has already been arrived to restaurant");
            }else {
         return ResponseEntity.ok("Delivery has not already arrived restaurant");
            }
        } else  {
            return ResponseEntity.badRequest().body("Your account is disable because of unavailable of your orders");
        }
}
@PostMapping("/give_time")
public ResponseEntity<String> getInfo(@Param("num_time")String num_time, @Param("type_time")String type_time,@RequestHeader("Authorization")String token) {
    User user= userRepository.findByEmails(jwtService.extractUsername(token));
    if(user.getRole()== Role.DELIVERY &&isDeliverOrdered(user.getEmail())){
        String s = num_time + type_time + " ning ichida kuryer sizga yetiboradi";
        clientDeliverOrderDtoList.forEach(
                clientDeliverOrderDto->{
                    if (clientDeliverOrderDto.getDeliveryEmail().equals(user.getEmail())) {
                        clientDeliverOrderDto.setInfo(s);
                    }
                }
        );
        return ResponseEntity.ok("info muvaffaqiyatli jonatilindi");
    }else{
        return ResponseEntity.badRequest().body("unacceptable role");
    }
}
@PostMapping("/info")
    public ResponseEntity<String> getOrderInfo(@RequestHeader("Authorization")String token) {
    String email=findUsername(token);
if (isClientOrdered(email)) {
    return ResponseEntity.ok(info + ". Faqat bir iltimos order/verify yo'liga qarang sizga kuryer kelib buyurtmangizni topshirsa tasdiqlaysiz");
}return ResponseEntity.badRequest().body("unacceptable email");
}

@PostMapping("/end")
    public ResponseEntity<String> endOrder(@Param("result")String resultOfOrder,@RequestHeader("Authorization")String token) {

    if(isDeliverOrdered(findUsername(token))){
        if (resultOfOrder.equals("ok")) {
          clientDeliverOrderDtoList.forEach(
                  clientDeliverOrderDto-> {
                  if (clientDeliverOrderDto.getDeliveryEmail().equals(findUsername(token))) {
                      clientDeliverOrderDto.setResult(true);
                  }
                  }
          );
            return ResponseEntity.ok("send msg");
        }else {
            return ResponseEntity.badRequest().body("correct message");
        }

    }else {
      return ResponseEntity.ok("unacceptable role");
    }
}
@PostMapping("/verify")
    public ResponseEntity<String> verifyOrder( @RequestHeader("Authorization")String token) {


    if(isResult(findUsername(token))&&isClientOrdered(findUsername(token))){
    return ResponseEntity.ok("sizga kuryer yetib keldi endi order/verifyTop orqali 1 raqamini yozib tasdiqlang tasdiqlang");
    }else {
        return ResponseEntity.ok("sizning buyurtmangiz hali jarayonda");
    }
}
@PostMapping("/verifyTop")
    public ResponseEntity<String> verifyTopOrder(@Param("res")String res,@RequestHeader("Authorization")String token) {

    if(isResultForClient(findUsername(token))&&findUsername(findUsername(token)).equals(userEmail)){
        if (res.equals("1")) {
            clientDeliverOrderDtoList.forEach(
                    clientDeliverOrderDto-> {
                        if (clientDeliverOrderDto.getClientEmail().equals(findUsername(token))) {
                            clientDeliverOrderDto.setResult(true);
                        }
                    }
            );
           return ResponseEntity.ok("buyurtma muvaffiqaytli yakunlandi");
        }
        else {
         return    ResponseEntity.ok("enter correct message");
        }
    }else{
        return ResponseEntity.badRequest().body("buyurtma yoq");
    }
}
@PostMapping("/cancel")
public ResponseEntity<String> removeOrder(@RequestHeader("Authorization")String token) {
    String email = findUsername(token);

    if (isClientOrdered(email)) {
        if (isArrivedToRestaurant(email)) {
            return ResponseEntity.ok("cancelled successfully");
        }else return ResponseEntity.badRequest().body("cancelled unsuccessfully because , the delivery had already arrived restaurant");
    }  else {
    return ResponseEntity.badRequest().body("unacceptable role");
    }
}
private boolean isClientOrdered(String email){

      for (ClientDeliverOrderDto clientDeliverOrderDto : clientDeliverOrderDtoList){
          if (clientDeliverOrderDto.getClientEmail().equals(email)) {
              return true;
          }
      }
        return false;
}

    private boolean isDeliverOrdered(String email){
        for (ClientDeliverOrderDto clientDeliverOrderDto : clientDeliverOrderDtoList){
            if (clientDeliverOrderDto.getDeliveryEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
    private  String findUsername (String token){

        String email;
        if(token.startsWith("Bearer ")){
            email = token.substring(7);
        }else email="null";
        return jwtService.extractUsername(email);}


    private List<ClientDeliverOrderDto> clientDeliverOrderDtos(){
        List<ClientDeliverOrderDto> clientDeliverOrderDtos=new ArrayList<>();
         clientDeliverOrderDtoList.forEach(
                 clientDeliverOrderDto -> {
                     if (clientDeliverOrderDto.getDeliveryEmail().isEmpty()) {
                         clientDeliverOrderDtos.add(clientDeliverOrderDto);
                     }
                 }
         );

return clientDeliverOrderDtos;}
    private boolean isTookOrder(String userEmail){
        AtomicBoolean isTookOrder= new AtomicBoolean(false);
        clientDeliverOrderDtoList.forEach(
                clientDeliverOrderDto -> {
                   isTookOrder.set(clientDeliverOrderDto.getClientEmail().equals(userEmail) && clientDeliverOrderDto.getDeliveryEmail().isEmpty());
                }
        );
   return isTookOrder.get(); }
private ClientDeliverOrderDto clientDeliverOrderDto(String userEmail){
       AtomicReference<ClientDeliverOrderDto> clientDeliverOrderDto= new AtomicReference<>(new ClientDeliverOrderDto());
        clientDeliverOrderDtoList.forEach(
                clientDeliverOrderDto1 -> {
                    if (clientDeliverOrderDto1.getClientEmail().equals(userEmail)) {
                        clientDeliverOrderDto.set(clientDeliverOrderDto1);
                    }
                }
        );
return clientDeliverOrderDto.get();}
private  void doEmptyDeliveryName(String userEmail){
        clientDeliverOrderDtoList.forEach(
                clientDeliverOrderDto -> {
                    if (clientDeliverOrderDto.getClientEmail().equals(userEmail)) {
                        clientDeliverOrderDto.setDeliveryEmail("");
                    }
                }
        );
}
private List<FoodCountDto> foodCountDto(String deliveryEmail){
        List<FoodCountDto> foodCountDtos=new ArrayList<>();
        clientDeliverOrderDtoList.forEach(
                clientDeliverOrderDto -> {
                    if (clientDeliverOrderDto.getDeliveryEmail().equals(deliveryEmail)) {
                        foodCountDtos.addAll(clientDeliverOrderDto.getFoodCountDtoList());
                    }
                }
        );
return foodCountDtos;}
private boolean isResult(String email){
        AtomicBoolean active= new AtomicBoolean(false);
    clientDeliverOrderDtoList.forEach(
            clientDeliverOrderDto-> {
                if (clientDeliverOrderDto.getDeliveryEmail().equals(email)) {
                active.set(clientDeliverOrderDto.isResult());
                }
            }
    );
return active.get();}
    private boolean isResultForClient(String email){
        AtomicBoolean active= new AtomicBoolean(false);
        clientDeliverOrderDtoList.forEach(
                clientDeliverOrderDto-> {
                    if (clientDeliverOrderDto.getDeliveryEmail().equals(email)) {
                        active.set(clientDeliverOrderDto.isResult());
                    }
                }
        );
        return active.get();}
    private boolean isArrivedToRestaurant(String userEmail){
        AtomicBoolean active= new AtomicBoolean(false);
        clientDeliverOrderDtoList.forEach(
                clientDeliverOrderDto-> {
                    if (clientDeliverOrderDto.getClientEmail().equals(userEmail)) {
                        active.set(clientDeliverOrderDto.isArrived());
                    }
                }
        );
        return active.get();
    }

    private void updateIsArrived(String deliveryEmail,boolean isArrived){
        clientDeliverOrderDtoList.forEach( clientDeliverOrderDto -> {
            if (clientDeliverOrderDto.getDeliveryEmail().equals(deliveryEmail)) {
                clientDeliverOrderDto.setArrived(isArrived);
            }
        });
    }
}
