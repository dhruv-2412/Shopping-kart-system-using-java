import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Product {
    protected String name;
    protected double price;
    protected int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}

class Clothing extends Product {
    private String brand;
    private String material;

    public Clothing(String name, double price, String brand, String material, int quantity) {
        super(name, price, quantity);
        this.brand = brand;
        this.material = material;
    }

   
}

public class OnlineStore {
    private static final String ITEMS_FILE = "items.txt";

    private List<Product> inventory;
    private List<Product> cart;

    public OnlineStore() {
        inventory = new ArrayList<>();
        cart = new ArrayList<>();
        loadInventory();
    }

    public void loadInventory() {
        try {
            File file = new File(ITEMS_FILE);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    int quantity = Integer.parseInt(parts[2].trim());
    
                    inventory.add(new Clothing(name, price, "", "", quantity));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    

    public void displayInventory() {
        System.out.println("Items Inventory:");
        for (int i = 0; i < inventory.size(); i++) {
            Product product = inventory.get(i);
            System.out.println((i + 1) + ". " + product.name + "- Price:" + product.price + " - Available Quantity: " + product.quantity);
        }
    }

    public void addToCart(int productIndex, int quantity) {
        if (productIndex >= 0 && productIndex < inventory.size()) {
            Product selectedProduct = inventory.get(productIndex);
            if (selectedProduct.quantity >= quantity) {
                for (int i = 0; i < quantity; i++) {
                    cart.add(selectedProduct);
                    selectedProduct.quantity--;
                }
                System.out.println(quantity + " " + selectedProduct.name + " added to cart.");
                updateInventoryFile();
            } else {
                System.out.println("Sorry, insufficient quantity of " + selectedProduct.name + ".");
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }

    public void updateInventoryFile() {
        try {
            FileWriter writer = new FileWriter(ITEMS_FILE);
            for (Product product : inventory) {
                writer.write(product.name + ", " + product.price + ", " + product.quantity + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayCart() {
        System.out.println("Items in the cart:");
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            for (Product product : cart) {
                System.out.println(product.name);
            }
        }
    }

    public static void main(String[] args) {
        OnlineStore store = new OnlineStore();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Store!");

        while (true) {
            System.out.println("Choose what you want to do:");
            System.out.println("1 - Display all items");
            System.out.println("2 - Add items to your Cart");
            System.out.println("3 - Display Cart");
            System.out.println("4 - Checkout");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    store.displayInventory();
                    break;
                case 2:
                    store.displayInventory();
                    System.out.println("Enter the number of the item to add to cart:");
                    int itemIndex = scanner.nextInt() - 1;
                    System.out.println("Enter the quantity:");
                    int quantity = scanner.nextInt();
                    store.addToCart(itemIndex, quantity);
                    break;
                case 3:
                    store.displayCart();
                    break;
                    case 4:
                    System.out.println("Checkout:");
                    System.out.println("Payment options: COD (Cash on Delivery) / Credit");
                    String paymentOption = scanner.next();
                    if (paymentOption.equalsIgnoreCase("COD")) {
                        System.out.println("Order confirmed. Your items will be delivered soon.");
                        System.out.println("Thank you for shopping with us!");
                        scanner.close();
                        System.exit(0);
                    } else if (paymentOption.equalsIgnoreCase("Credit")) {
                        System.out.println("Enter your 4-digit PIN:");
                        int pin = scanner.nextInt();
                        if (pin >= 1000 && pin <= 9999) {
                            System.out.println("Payment successful. Your order will be delivered soon.");
                            System.out.println("Thank you for shopping with us!");
                            scanner.close();
                            System.exit(0);
                        } else {
                            System.out.println("Invalid PIN. Payment failed.");
                        }
                    } else {
                        System.out.println("Invalid payment option. Please choose between COD or Credit.");
                    }
                    break;
                
            }
        }
    }
}
