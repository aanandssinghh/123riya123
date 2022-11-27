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

    public InvertedIndex(Map<String, Hotel> hotelMap) {
        this.hotelMap = hotelMap;
    }

    List<String> stopwords = Arrays.asList("a", "able", "about",
            "across", "after", "all", "almost", "also", "am", "among", "an",
            "and", "any", "are", "as", "at", "be", "because", "been", "but",
            "by", "can", "cannot", "could", "dear", "did", "do", "does",
            "either", "else", "ever", "every", "for", "from", "get", "got",
            "had", "has", "have", "he", "her", "hers", "him", "his", "how",
            "however", "i", "if", "in", "into", "is", "it", "its", "just",
            "least", "let", "like", "likely", "may", "me", "might", "most",
            "must", "my", "neither", "no", "nor", "not", "of", "off", "often",
            "on", "only", "or", "other", "our", "own", "rather", "said", "say",
            "says", "she", "should", "since", "so", "some", "than", "that",
            "the", "their", "them", "then", "there", "these", "they", "this",
            "tis", "to", "too", "twas", "us", "wants", "was", "we", "were",
            "what", "when", "where", "which", "while", "who", "whom", "why",
            "will", "with", "would", "yet", "you", "your");

    // mapping of word with list of documentIndex where word appears.
    // TODO: use hash set instead of List<integer> to avoid adding hotel at the end
    // of the list, if we crawl the same page again.
    Map<String, HashSet<String>> indexOfHotelList = new HashMap<>();
    Map<String, Hotel> hotelMap;

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

    public void printIndex() {
        System.out.println(this.indexOfHotelList.toString());
    }
}