package nu.staldal.zt;

public class FruitBean {
    
    private final String name;
    private final String taste;
    private final String color;

    public FruitBean(String name, String taste, String color) {
        this.name = name;
        this.taste = taste;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getTaste() {
        return taste;
    }

    public String getColor() {
        return color;
    }
    
}
