package main;

import java.util.*;
import java.util.stream.Stream;

public class SizeOrder {
    private String[] sizes;
    private final List<String> literal = new ArrayList<>();
    private final Map<String, List<Float>> mapSize = new LinkedHashMap<>();
    private final List<String> exceptions = new ArrayList<>();

    public SizeOrder(String[] sizes) {
        this.sizes = sizes;
        divideSizesByType();
    }

    private void divideSizesByType(){
        for (String size : sizes) {
            if (isNumber(size)){
                if (!mapSize.containsKey("Numerical")) {
                    mapSize.put("Numerical", new ArrayList<>());
                }
                mapSize.get("Numerical").add(Float.parseFloat(size));
            } else if (isOnlyLiteral(size) || isQuantifiedLiteral(size)){
                literal.add(size);
            } else if (isNationalSizeType(size) || isNationalSizeTypeWithoutShift(size)){//for simplicity national size are written number+" "+identifier or in opposed order with two literal identifier
                if (isNationalSizeType(size)){
                    nationalSizeTypes(size);
                } else {
                    String shiftSize = addShift(size);
                    nationalSizeTypes(shiftSize);
                }
            } else {
                this.exceptions.add(size);
            }
        }
    }

    private String addShift(String size) {
        if (size == null || size.length() < 3) {
            return size;
        }
        if (Character.isLetter(size.charAt(0)) && Character.isLetter(size.charAt(1))) {
            return size.substring(0, 2) + " " + size.substring(2);
        }
        int len = size.length();
        if (Character.isLetter(size.charAt(len - 1)) && Character.isLetter(size.charAt(len - 2))) {
            return size.substring(0, len - 2) + " " + size.substring(len - 2);
        }
        return size;
    }

    private boolean isQuantifiedLiteral(String size) {
        return size.length() > 2 &&
                (
                        (
                                size.charAt(size.length() - 2) == 'X' && getLastChar(size) == 'L'
                        ) || (
                                size.charAt(size.length() - 2) == 'X' && getLastChar(size) == 'S'
                        )
                )
                && isPartiallyNumber(size,2);
    }

    private boolean isPartiallyNumber(String s, int end) {
        for(int i = 0; i < s.length() - end; i++){
            char c = s.charAt(i);
            if(!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }

    int getPartiallyNumber(String s) {
        String digits = s.replaceAll("\\D", "");
        return Integer.parseInt(digits);
    }

    private boolean isNationalSizeType(String size){
        String[] element = size.split(" ");
        return element.length == 2 && !isNumber(element[0]) && isNumber(element[1])
                || element.length == 2 && isNumber(element[0]) && !isNumber(element[1]);
    }

    private boolean isNationalSizeTypeWithoutShift(String size){
        return (!Character.isDigit(size.charAt(size.length() - 1)) && !Character.isDigit(size.charAt(size.length() - 2)))
                || !Character.isDigit(size.charAt(0)) && !Character.isDigit(size.charAt(1));
    }

    private void nationalSizeTypes(String size){
        String[] element = size.split(" ");
        if(!isNumber(element[0])){
            if (!mapSize.containsKey(element[0])) {
                mapSize.put(element[0], new ArrayList<>());
            }
            mapSize.get(element[0]).add(Float.parseFloat(element[1]));
        } else {
            if (!mapSize.containsKey(element[1])) {
                mapSize.put(element[1], new ArrayList<>());
            }
            mapSize.get(element[1]).add(Float.parseFloat(element[0]));
        }

    }

    private boolean isOnlyLiteral(String size) {
        for(int i = 0; i < size.length(); i++){
            char c = size.charAt(i);
            if(Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }

    private boolean isNumber(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        } else {
            try {
                Float.parseFloat(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    private String[] orderMapSize() {
        List<String> sizes = new ArrayList<>();
        for(String key : mapSize.keySet()){
            if(!Objects.equals(key, "Numerical")){
                Collections.sort(mapSize.get(key));
                for(float f : mapSize.get(key)){
                    sizes.add(key + " " + f);
                }
            } else {
                Collections.sort(mapSize.get(key));
                for(float f : mapSize.get(key)){
                    sizes.add(""+f);
                }
            }
        }
        return sizes.toArray(new String[0]);
    }

    int getXLength(String size){
        int xLength = 0;
        if (getLastChar(size) == 'L' || getLastChar(size) == 'S') {
            xLength = size.length()-1;
            if (isNationalSizeType(size)) {
                xLength = getPartiallyNumber(size);
            }
        }
        return xLength;
    }

    private String[] orderListSize() {
        List<String> newSizes = new LinkedList<>();
        for(String size : literal){
            if(newSizes.isEmpty()){
                newSizes.add(size);
            } else if (newSizes.contains(size)) {
                newSizes.add(newSizes.indexOf(size),size);
            } else {
                int xLength = getXLength(size);
                if (getLastChar(size) == 'L') {
                    if(getLastChar(newSizes.getLast()) != 'L'
                            || newSizes.getLast().length() <= xLength+1){
                        newSizes.add(size);
                    } else {
                        newSizes.add(findFirstHigherIndexL(size,newSizes),size);
                    }
                } else if (getLastChar(size) == 'S') {
                    if(getLastChar(newSizes.getFirst()) != 'S'
                            || newSizes.getFirst().length() <= xLength+1){
                        newSizes.addFirst(size);
                    } else {
                        newSizes.add(findFirstHigherIndexS(size,newSizes),size);
                    }
                } else if (getLastChar(newSizes.getLast()) != 'L') {
                    newSizes.addLast(size);
                } else {
                    newSizes.add(findFirstIndexL(size,newSizes),size);
                }
            }
        }
        return newSizes.toArray(new String[0]);
    }

    private int findFirstIndexL(String size, List<String> sizes) {
        int i = 0;
        while (i < sizes.size()
                && getLastChar(sizes.get(i)) != 'L') {
            i++;
        }
        return i;
    }

    private int findFirstHigherIndexS(String size, List<String> sizes) {
        int i = 0;
        int sizeLength = getXLength(size) + 1;
        while (i < sizes.size()
                && sizes.get(i).length() > sizeLength) {
            if (getLastChar(sizes.get(i))=='M'
                    || getLastChar(sizes.get(i)) == 'L') break;
            i++;
        }
        return i;
    }

    private int findFirstHigherIndexL(String size, List<String> sizes) {
        int i = 0;
        int sizeLength = getXLength(size) + 1;
        while (i < sizes.size()) {
            if (getLastChar(sizes.get(i)) == 'L' && sizes.get(i).length() > sizeLength) {
                break;
            }
            i++;
        }
        return i;
    }

    public String[] orderList(){
        String[] orderedArray = Stream.concat(Arrays.stream(orderListSize()),
                        Arrays.stream(orderMapSize()))
                            .toArray(String[]::new);
        String[] sortedArray = Stream.concat(Arrays.stream(orderedArray), Arrays.stream(exceptions.toArray(new String[0]))).toArray(String[]::new);
        this.sizes = sortedArray;
        return sortedArray;
    }

    private char getLastChar(String size){
        return size.charAt(size.length()-1);
    }

    public String toString(){
        orderList();
        StringBuilder lineString = new StringBuilder(Arrays.toString(orderListSize()));
        lineString.append("\n");
        for (Map.Entry<String, List<Float>> entry : this.mapSize.entrySet()) {
            String key = entry.getKey();
            List<Float> values = entry.getValue();
            StringJoiner joiner = new StringJoiner(", ");
            for (Float value : values) {
                if(!key.equals("Numerical")){
                    joiner.add(key + " " + value);
                } else {
                    joiner.add(value.toString());
                }
            }
            lineString.append("[").append(joiner.toString()).append("]").append("\n");
        }
        if(!exceptions.isEmpty()){
            lineString.append("Exceptions:\n").append(exceptions);
        }
        return lineString.toString();
    }

}
