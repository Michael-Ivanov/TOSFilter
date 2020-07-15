import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

public class Symbol {

    private String name;
    private double price;
    private String time;
    private List<Integer> parts;


    public Symbol() {
        parts = new ArrayList<>();
    }

    public Symbol(String name) {
        this.name = name;
        this.time = "00:00:00";
        parts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void addPart (int part) {
        if (parts.size() <= 5) {
            parts.add(part);
        }
    }
    public List<Integer> getParts() {
        return parts;
    }

    public void clearParts() {
        parts.clear();
    }

    @Override
    public String toString() {
        return "Symbol {" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", parts=" + parts +
                '}';
    }
}
