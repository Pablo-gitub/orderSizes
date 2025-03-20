package test;

import main.SizeOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SizeOrderTest {
    SizeOrder sizes;

    @Test
    public void givenTest() {
        String[] sizesArray = {"S", "43", "XL", "40", "M", "12", "IT 35", "IT 43", "FR 12", "UK 50", "XXL", "IT 50"};
        sizes = new SizeOrder(sizesArray);
        String[] expected = new String[]{
                "S", "M", "XL", "XXL", "12.0",
                "40.0", "43.0", "IT 35.0", "IT 43.0",
                "IT 50.0", "FR 12.0", "UK 50.0"};
        assertArrayEquals(expected, sizes.orderList());
    }

    @Test
    public void test() {
        String[] sizesArray = {"XS", "43", "XL", "40", "M", "12", "IT 35", "IT 43", "FR 12", "UK 50", "S", "IT 50"};
        sizes = new SizeOrder(sizesArray);
        String[] expected = new String[]{
                "XS", "S", "M", "XL",
                "12.0", "40.0", "43.0",
                "IT 35.0", "IT 43.0", "IT 50.0",
                "FR 12.0", "UK 50.0"};
        assertArrayEquals(expected, sizes.orderList());
    }

    @Test
    public void testOnlyLiteralSizes() {
        String[] sizesArray = {"XS", "XL", "M", "S", "L", "XXS", "XXL"};
        sizes = new SizeOrder(sizesArray);
        String[] expected = new String[]{"XXS", "XS","S", "M", "L","XL", "XXL"};
        assertArrayEquals(expected, sizes.orderList());
    }

    @Test
    public void testOnlyNumerical() {
        String[] sizesArray = {"43", "12", "40", "99"};
        sizes = new SizeOrder(sizesArray);
        String[] expected = new String[]{"12.0", "40.0", "43.0", "99.0"};
        assertArrayEquals(expected, sizes.orderList());
    }

    @Test
    public void testOnlyCompositeSizes() {
        String[] sizesArray = {"IT 43", "UK 50", "FR 12", "IT 35", "IT 50"};
        sizes = new SizeOrder(sizesArray);
        String[] expected = new String[]{"IT 35.0", "IT 43.0", "IT 50.0", "UK 50.0", "FR 12.0"};
        assertArrayEquals(expected, sizes.orderList());
    }

    @Test
    public void testMixedSizes() {
        String[] sizesArray = {"XXS", "50", "IT 35", "M", "12", "UK 50", "XL", "43", "IT 43"};
        sizes = new SizeOrder(sizesArray);
        String[] expected = new String[]{
                "XXS", "M", "XL",
                "12.0", "43.0", "50.0",
                "IT 35.0", "IT 43.0", "UK 50.0"
        };
        assertArrayEquals(expected, sizes.orderList());
    }
}
