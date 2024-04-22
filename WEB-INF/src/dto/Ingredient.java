package dto;

public class Ingredient{

    private int id = -1;
    private String name;
    private double price;

    public Ingredient(int id, String name, double price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Ingredient(){}

    public Ingredient(String name2, double price2) {
        this(-1,name2,price2);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Ingredient [id=" + id + ", name=" + name + ", price=" + price + "]";
    }
}