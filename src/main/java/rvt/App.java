package rvt;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CategoryDAO categoryDAO = new CategoryDAO();
    private static final ProductDAO productDAO = new ProductDAO();

    public static void main(String[] args) {
        printWelcome();
        while (true) {
            printMenu();
            String selection = scanner.nextLine().trim();
            if (selection.isEmpty()) {
                continue;
            }
            try {
                switch (selection) {
                    case "1" -> addCategory();
                    case "2" -> addProduct();
                    case "3" -> showCategories();
                    case "4" -> showProducts();
                    case "5" -> searchProductsByCategory();
                    case "0" -> {
                        System.out.println("Programma tiek slēgta. Uz redzēšanos!");
                        return;
                    }
                    default -> System.out.println("Nederīga izvēle. Mēģiniet vēlreiz.");
                }
            } catch (SQLException ex) {
                System.out.println("Datu bāzes kļūda: " + ex.getMessage());
            }
        }
    }

    private static void printWelcome() {
        System.out.println("Produktu un kategoriju pārvaldības sistēma");
        System.out.println("SQLite datubāze un droša PreparedStatement izmantošana");
        System.out.println();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1 - Pievienot kategoriju");
        System.out.println("2 - Pievienot produktu");
        System.out.println("3 - Parādīt visas kategorijas");
        System.out.println("4 - Parādīt visus produktus");
        System.out.println("5 - Meklēt produktus pēc kategorijas");
        System.out.println("0 - Iziet");
        System.out.print("Izvēlies darbību: ");
    }

    private static void addCategory() throws SQLException {
        System.out.print("Ievadi kategorijas nosaukumu: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Nosaukums nedrīkst būt tukšs.");
            return;
        }
        int id = categoryDAO.createCategory(name);
        System.out.println("Kategorija pievienota ar ID " + id + ".");
    }

    private static void addProduct() throws SQLException {
        List<Category> categories = categoryDAO.findAll();
        if (categories.isEmpty()) {
            System.out.println("Lūdzu vispirms pievieno kategoriju.");
            return;
        }
        showCategories();

        System.out.print("Ievadi produkta nosaukumu: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Produkta nosaukums nedrīkst būt tukšs.");
            return;
        }
        System.out.print("Ievadi produkta cenu: ");
        String priceInput = scanner.nextLine().trim();
        double price;
        try {
            price = Double.parseDouble(priceInput);
        } catch (NumberFormatException ex) {
            System.out.println("Cena jāievada kā skaitlis.");
            return;
        }
        if (price <= 0) {
            System.out.println("Cena jābūt lielākai par 0.");
            return;
        }

        System.out.print("Ievadi kategorijas ID: ");
        String categoryIdInput = scanner.nextLine().trim();
        int categoryId;
        try {
            categoryId = Integer.parseInt(categoryIdInput);
        } catch (NumberFormatException ex) {
            System.out.println("Kategorijas ID jāievada kā vesels skaitlis.");
            return;
        }
        if (categoryDAO.findById(categoryId).isEmpty()) {
            System.out.println("Kategorija ar ID " + categoryId + " neeksistē.");
            return;
        }

        int id = productDAO.createProduct(name, price, categoryId);
        System.out.println("Produkts pievienots ar ID " + id + ".");
    }

    private static void showCategories() throws SQLException {
        List<Category> categories = categoryDAO.findAll();
        if (categories.isEmpty()) {
            System.out.println("Nav pieejamu kategoriju.");
            return;
        }
        System.out.println("Kategorijas:");
        categories.forEach(category -> System.out.println("  " + category));
    }

    private static void showProducts() throws SQLException {
        List<Product> products = productDAO.findAll();
        if (products.isEmpty()) {
            System.out.println("Nav pieejamu produktu.");
            return;
        }
        System.out.println("Produkti:");
        products.forEach(product -> System.out.println("  " + product));
    }

    private static void searchProductsByCategory() throws SQLException {
        System.out.print("Ievadi kategorijas ID vai nosaukumu: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Ievadiet kategorijas ID vai nosaukumu.");
            return;
        }
        List<Product> products;
        Optional<Integer> maybeId = parseInteger(input);
        if (maybeId.isPresent()) {
            products = productDAO.findByCategoryId(maybeId.get());
        } else {
            products = productDAO.findByCategoryName(input);
        }
        if (products.isEmpty()) {
            System.out.println("Nav atrasti produkti pēc norādītās kategorijas.");
            return;
        }
        System.out.println("Atrasti produkti:");
        products.forEach(product -> System.out.println("  " + product));
    }

    private static Optional<Integer> parseInteger(String text) {
        try {
            return Optional.of(Integer.parseInt(text));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
}
