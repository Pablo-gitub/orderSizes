package main;

import java.util.*;
import java.util.stream.Stream;

public class SizeOrder {
    private String[] sizes;
    private final List<String> literal = new ArrayList<>();
    private final Map<String, List<Float>> mapSize = new LinkedHashMap<>();

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
            } else if (isOnlyLiteral(size)){
                literal.add(size);
            } else {//for simplicity from the given example I suppose that compose size are written number+""+identifier
                String[] element = size.split(" ");
                if (!mapSize.containsKey(element[0])) {
                    mapSize.put(element[0], new ArrayList<>());
                }
                mapSize.get(element[0]).add(Float.parseFloat(element[1]));
            }
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
                    sizes.add(f + " " + key);
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

    private String[] orderListSize() {
        List<String> newSizes = new LinkedList<>();
        for(String size : literal){
            if(newSizes.isEmpty()){
                newSizes.add(size);
            } else if (newSizes.contains(size)) {
                newSizes.add(newSizes.indexOf(size),size);
            } else {
                if (size.charAt(size.length()-1) == 'L') {
                    if(getLastChar(newSizes.getLast()) != 'L'
                            || newSizes.getLast().length() <= size.length()){
                        newSizes.add(size);
                    } else {
                        newSizes.add(findFirstHigherIndexL(size,newSizes),size);
                    }
                } else if (getLastChar(size) == 'S') {
                    if(getLastChar(newSizes.getFirst()) != 'S'
                            || newSizes.getFirst().length() <= size.length()){
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
        while (i < sizes.size()
                && sizes.get(i).length() > size.length()) {
            if (sizes.get(i).charAt(sizes.get(i).length()-1)=='M'
                    || sizes.get(i).charAt(sizes.get(i).length()-1) == 'L') break;
            i++;
        }
        return i;
    }

    private int findFirstHigherIndexL(String size, List<String> sizes) {
        int i = 0;
        while (i < sizes.size()) {
            if (getLastChar(sizes.get(i)) == 'L' && sizes.get(i).length() > size.length()) {
                break;
            }
            i++;
        }
        return i;
    }

    public String[] orderList(){
        String[] orderedArray = Stream.concat(Arrays.stream(orderListSize()), Arrays.stream(orderMapSize()))
                .toArray(String[]::new);
        this.sizes = orderedArray;
        return orderedArray;
    }

    private char getLastChar(String size){
        return size.charAt(size.length()-1);
    }

}
