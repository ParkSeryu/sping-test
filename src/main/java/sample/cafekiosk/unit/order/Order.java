package sample.cafekiosk.unit.order;

import java.time.LocalDateTime;
import java.util.List;
import sample.cafekiosk.unit.beverage.Beverage;

public class Order {

    private LocalDateTime orderTime;
    private List<Beverage> beverages;


    public Order(LocalDateTime now, List<Beverage> beverages) {
    }
}
