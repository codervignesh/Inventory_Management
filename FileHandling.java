package usingFiles;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandling {
    private static final String PRODUCTS_FILE_PATH = "/Users/vignesh/IdeaProjects/InventoryManagement/src/main/java/usingFiles/products.txt";
    private static final String REPORT_FILE_PATH = "/Users/vignesh/IdeaProjects/InventoryManagement/src/main/java/usingFiles/reports.txt";
    private static final String LOG_FILE_PATH = "/Users/vignesh/IdeaProjects/InventoryManagement/src/main/java/usingFiles/log.txt";

    public static void generateReport(String path, ArrayList<Product> products){
        clearFileContents(path);
        String prodLine;
        float totalValue = 0;
        int totalQuantity = 0;
        includeReportHeader();
        for (Product prod : products) {
            String formatStr = "%-10s %-20s %-15s %-15s %-25s%n";
            prodLine = String.format(formatStr, prod.productId, prod.productName, prod.productPrice, prod.productQuantity, prod.productPrice*prod.productQuantity);
            modifyFile(path, true, prodLine + "\n");
            totalValue += prod.productPrice*prod.productQuantity;
            totalQuantity += prod.productQuantity;
        }
        includeReportFooter(totalQuantity, totalValue);
    }

    private static void includeReportFooter(int totQuantity, float totValue) {
        String formatStr = "%-10s %-20s %-15s %-15s %-25s%n";
        String heading = String.format(formatStr, "Total" , "", "", totQuantity, totValue);
        modifyFile(REPORT_FILE_PATH, true, heading + "\n");
    }

    private static void includeReportHeader() {
        String formatStr = "%-10s %-20s %-15s %-15s %-25s%n";
        String heading = String.format(formatStr, "Product Id" , "Product Name", "Product Price", "Product Quantity", "Product Value");
        modifyFile(REPORT_FILE_PATH, true, heading + "\n");
    }

    public static ArrayList<Product> readProducts(String path) {
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            File f1 = new File(path);
            Scanner dataReader = new Scanner(f1);
            while (dataReader.hasNextLine()) {
                String fileData = dataReader.nextLine();
                String[] prod = fileData.split(", ");
                products.add(new Product(prod[0], prod[1], Float.parseFloat(prod[2]), Integer.parseInt(prod[3])));
            }
            dataReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static void updateProducts(ArrayList<Product> products) {
        clearFileContents(PRODUCTS_FILE_PATH);

        String prodLine;
        for (Product prod : products) {
            prodLine = prod.productId + ", " + prod.productName + ", " + Float.toString(prod.productPrice) + ", " + Integer.toString(prod.productQuantity);
            modifyFile(PRODUCTS_FILE_PATH, true, prodLine + "\n");
        }
    }

    public static void clearFileContents(String path) {
        modifyFile(path, false, "");
    }

    public static void addLog(String text){
        modifyFile(LOG_FILE_PATH, true, text);
    }

    public static void modifyFile(String path, Boolean append, String content){
        try {
            FileWriter writer = new FileWriter(path, append);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}