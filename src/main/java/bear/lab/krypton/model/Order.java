package bear.lab.krypton.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Order {

    int id;
    int petId;
    int quantity;
    String shipDate;
    String status;
    boolean complete;

    public static Order get() {
        return new Order();
    }

    public Order() {
        this.id = 1;
        this.petId = 2;
        this.quantity = 2;
        this.shipDate = LocalDateTime.now().toString();
        this.status = "approved";
        this.complete = true;
    }

    public Order(int id, int petId, int quantity, String shipDate, String status, boolean complete) {
        this.id = id;
        this.petId = petId;
        this.quantity = quantity;
        this.shipDate = shipDate;
        this.status = status;
        this.complete = complete;
    }


}
