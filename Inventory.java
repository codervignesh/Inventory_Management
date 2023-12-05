package usingFiles;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Inventory {
    ArrayList<Product> products;

    Inventory(){   //fetch read products from file
        products = FileHandling.readProducts("/Users/vignesh/IdeaProjects/InventoryManagement/src/main/java/usingFiles/products.txt");
    }

    public void addProduct(){
        Scanner sc = new Scanner(System.in);
        String prodId;

        do{
            System.out.println("\nEnter Product ID: ");
            prodId = sc.next();
            if(isProdIDDuplicate(prodId))
                System.out.print("Product Id Already available");
        }while(isProdIDDuplicate(prodId));

        System.out.println("Enter Product Name: ");
        String prodName = sc.next();
        System.out.println("Enter Product Price: ");
        float prodPrice = sc.nextFloat();
        int prodQuantity;

        do {
            System.out.println("Enter Product Quantity: ");
            prodQuantity = sc.nextInt();
        }while (prodQuantity < 0);

        products.add(new Product(prodId, prodName, prodPrice, prodQuantity));
        FileHandling.updateProducts(products);
        FileHandling.addLog("\nProduct with id " + prodId + " added to the inventory\n");
    }

    private boolean isProdIDDuplicate(String pId) {
        for(Product p : this.products)
            if(p.productId.equals(pId))
                return true;
        return false;
    }

    public synchronized void makePurchase() {
        if(checkInventoryIsEmpty())
            return;
        this.displayProducts();
        Scanner sc = new Scanner(System.in);

        String id;
        int quantity;

        try {
//            do {
                System.out.println("\nEnter Product ID: ");
                id = sc.next();
                System.out.println("Enter Quantity to purchase: ");
                quantity = sc.nextInt();
//            } while (updateQuantityOnPurchase(id, quantity));
            Thread threadObject = new PurchaseMultiThread(id , quantity);
            threadObject.start();
            displayProductById(id);
        } catch (InputMismatchException e){
            System.out.println(e);
        }
    }

    private void displayProducts() {
        if(checkInventoryIsEmpty())
            return;
        for(Product p : products){
            System.out.println("\nProduct ID: " + p.productId + "\nProduct Name: " + p.productName + "\nPrice: " + p.productPrice
                    + "\nQuantity Available: " + p.productQuantity);
        }
    }

    public Boolean updateQuantityOnPurchase(String id, int quantity) {
        boolean flag = true;
        for (Product p : products) {
            if(p.productId.equals(id) && p.productQuantity > quantity && p.productQuantity > 1) {
                p.productQuantity -= quantity;
                System.out.println(p.productQuantity + " " + p.productId);
                FileHandling.updateProducts(products);
                FileHandling.addLog("\n" + quantity + " quantity of Product with id " + id + " is purchased\n");
                flag = false;
            }
        }
        if(flag) {
            System.out.println("Invalid Product id or Quantity");
            return true;
        }
        return false;
    }

    public void displayProductById(String id) {
        boolean isPresent = false;
        if(checkInventoryIsEmpty())
            return;
        for(Product p : products){
            if(p.productId.equals(id)) {
                System.out.println("\nProduct ID: " + p.productId + "\nProduct Name: " + p.productName + "\nProduct Price: " + p.productPrice
                        + "\nProduct Quantity: " + p.productQuantity + "\nTotal value: " + p.productQuantity * p.productPrice);
                isPresent = true;
            }
        }
        if (!isPresent)
            System.out.println("No product with id: " + id);
    }

    public boolean checkInventoryIsEmpty(){
        if(products.isEmpty()) {
            System.out.println("Inventory is Empty");
            return true;
        }
        return false;
    }

    public void generateReport() {
        FileHandling.generateReport("/Users/vignesh/IdeaProjects/InventoryManagement/src/main/java/usingFiles/reports.txt", products);
        if(checkInventoryIsEmpty())
            return;
        float totalValue = 0;
        for(Product p : products){
            System.out.println("\nProduct ID: " + p.productId + "\nProduct Name: " + p.productName + "\nProduct Price: " + p.productPrice
                    + "\nProduct Quantity: " + p.productQuantity + "\nTotal value: " + p.productQuantity*p.productPrice);
            totalValue += p.productPrice*p.productQuantity;
        }
        System.out.println("Total Inventory value: " + totalValue);

    }
}

//class PurchaseMultiThread extends Thread{
//    String id;
//    int quantity;
//    PurchaseMultiThread(String id , int quantity)
//    {
//        this.id = id;
//        this.quantity = quantity;
//    }
//    public void run()
//    {
//        try{
//            Inventory inv = new Inventory();
//            inv.updateQuantityOnPurchase(id , quantity);
//        }catch(Exception e) {
//            System.out.println("Caught the exception");
//        }
//    }
//}