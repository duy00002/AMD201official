import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OrderQueue implements Serializable {
    private static final long serialVersionUID = 1L;
    private final LinkedList<Order> orders = new LinkedList<>();

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getAllOrders() {
        return new LinkedList<>(orders);
    }

    public void removeOrderById(int id) {
        Optional<Order> orderToRemove = orders.stream()
                                              .filter(order -> order.getId() == id)
                                              .findFirst();
        orderToRemove.ifPresent(orders::remove);
    }
}