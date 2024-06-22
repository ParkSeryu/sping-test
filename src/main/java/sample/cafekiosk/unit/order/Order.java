package sample.cafekiosk.unit.order;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;

@Getter
public class Order {

    private LocalDateTime orderTime;
    private List<Beverage> beverages;

    public Order(LocalDateTime orderTime, List<Beverage> beverages) {
        this.orderTime = orderTime;
        this.beverages = beverages;
    }
}
