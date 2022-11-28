package hotelprice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {

    Map<String, HashSet<String>> indexOfHotelList = new HashMap<>();
    Map<String, Hotel> hotelMap;

    public InvertedIndex(Map<String, Hotel> hotelMap) {
        this.hotelMap = hotelMap;
    }

    public void addToIndex(Hotel hotel) {
        String[] words = hotel.getWords();
        String hotelName = hotel.getName();

        for (String w : new HashSet<String>(Arrays.asList(words))) {
            String word = w.toLowerCase();
            HashSet<String> hotelNameList = indexOfHotelList.get(word);
            if (hotelNameList == null) {
                hotelNameList = new HashSet<String>();
                indexOfHotelList.put(word, hotelNameList);
            }
            hotelNameList.add(hotelName);
        }

    }

    public void createIndex() {
        for (Hotel hotel : hotelMap.values()) {
            addToIndex(hotel);
        }
        // System.out.println(indexList.toString());
    }

    // return set of document indexes which contains the following words
    public Set<String> search(String[] words) {
        Set<String> hotelSet = new HashSet<>();
        for (String w : words) {
            String word = w.toLowerCase();
            HashSet<String> hotelNamesList = indexOfHotelList.get(word);
            if (hotelNamesList != null) {
                for (String hotel : hotelNamesList) {
                    hotelSet.add(hotel);
                }
            }
        }
        return hotelSet;
    }

}