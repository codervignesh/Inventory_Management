package usingFiles;

import java.util.Scanner;

public class Product {
    String productId;
    String productName;
    public float productPrice;
    int productQuantity;

    Product(String prodId, String prodName, float prodPrice, int prodQuantity){
        this.productId = prodId;
        this.productName = prodName;
        this.productPrice = prodPrice;
        this.productQuantity = prodQuantity;
    }

    public static void main(String[] args) {
        Inventory inventoryCbe = new Inventory();
        Scanner sc = new Scanner(System.in);
        int choice;
        do{
            System.out.println("\nEnter \n 1. Add products \n 2. Make a purchase \n 3. Retrieve product details by ID \n 4. Generate Report " +
                    "\n 0. Exit");
            System.out.print("Choice: ");
            choice = sc.nextInt();
            switch (choice) {
                case 0:
                    break;
                case 1:
                    inventoryCbe.addProduct();
                    break;
                case 2:
                    inventoryCbe.makePurchase();
                    break;
                case 3:
                    if(inventoryCbe.checkInventoryIsEmpty())
                        break;
                    System.out.println("\n Enter Product Id: ");
                    String id = sc.next();
                    inventoryCbe.displayProductById(id);
                    break;
                case 4:
                    inventoryCbe.generateReport();
                    break;
                default:
                    System.out.println("Invalid Input");
                    break;
            }
        }while(choice != 0);
    }
}

class PurchaseMultiThread extends Thread{
    String id;
    int quantity;
    PurchaseMultiThread(String id , int quantity)
    {
        this.id = id;
        this.quantity = quantity;
    }
    public void run()
    {
        try{
            Inventory inv = new Inventory();
            inv.updateQuantityOnPurchase(id , quantity);
        }catch(Exception e) {
            System.out.println("Caught the exception");
        }
    }
}