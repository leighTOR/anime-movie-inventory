public class AnimePurchase {
    private int id;
    private String title;
    private int quantity;
    private double price;

    // Constructor for existing rows (with ID)
    public AnimePurchase(int id, String title, int quantity, double price) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
    }

    // Constructor for new rows (ID auto-generated)
    public AnimePurchase(String title, int quantity, double price) {
        this.title = title;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
