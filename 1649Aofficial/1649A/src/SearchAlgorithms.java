
import java.util.List;
import java.util.stream.Collectors;

public class SearchAlgorithms {
    public static List<Order> linearSearch(List<Order> orders, String idText, String nameText, String addressText, String bookAndQuantitiesText) {
        return orders.stream().filter(order -> 
            (!idText.isEmpty() && String.valueOf(order.getId()).contains(idText)) ||
            (!nameText.isEmpty() && order.getName().contains(nameText)) ||
            (!addressText.isEmpty() && order.getAddress().contains(addressText)) ||
            (!bookAndQuantitiesText.isEmpty() && order.getBookAndQuantities().contains(bookAndQuantitiesText))
        ).collect(Collectors.toList());
    }
}