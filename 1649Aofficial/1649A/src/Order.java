import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    private final int id;
    private final String name;
    private final String address;
    private final String bookAndQuantities;

    public Order(String name, String address, String bookAndQuantities) {
        this.id = idGenerator.incrementAndGet();
        this.name = name;
        this.address = address;
        this.bookAndQuantities = bookAndQuantities;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBookAndQuantities() {
        return bookAndQuantities;
    }
}