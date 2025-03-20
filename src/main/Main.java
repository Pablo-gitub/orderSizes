package main;

public class Main {
    public static void main(String[] args) {
        String[] sizesArray = {"S", "43", "XL", "40", "M", "12", "XXXXS","IT 35","2XL","ES10", "IT 43", "FR 12", "UK 50", "XXL", "IT 50"};
        SizeOrder sizes = new SizeOrder(sizesArray);
        String[] orderedArray = sizes.orderList();
        for (String s : orderedArray) {
            System.out.println(s);
        }

        String[] newSizesArray = {"XS", "XL", "M", "S", "L", "XXS", "XXL"};
        sizes = new SizeOrder(newSizesArray);
        orderedArray = sizes.orderList();
        for (String s : orderedArray) {
            System.out.println(s);
        }
    }
}