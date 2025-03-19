package main;

public class Main {
    public static void main(String[] args) {
        String[] sizesArray = {"S", "43", "XL", "40", "M", "12", "IT 35", "IT 43", "FR 12", "UK 50", "XXL", "IT 50"};
        SizeOrder sizes = new SizeOrder(sizesArray);
        String[] orderedArray = sizes.orderList();
        for (String s : orderedArray) {
            System.out.println(s);
        }

        String[] sizesArray2 = {"XS", "XL", "M", "S", "L", "XXS", "XXL"};
        sizes = new SizeOrder(sizesArray2);
        String[] orderedArray2 = sizes.orderList();
        for (String s : orderedArray2) {
            System.out.println(s);
        }
    }
}