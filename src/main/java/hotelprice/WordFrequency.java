package hotelprice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WordFrequency {
    // first integer is for documentIndex, and map represents word to count mapping.
    // count is document-wise for the given word.
    Map<String, Map<String, Integer>> frequencyMapOfEachHotel = new HashMap<>();
    Map<String, Hotel> hotelMap;

    public WordFrequency(Map<String, Hotel> hotelMap) {
        this.hotelMap = hotelMap;
    }

    public void setWordFrequencies() {
        for (Hotel hotel : hotelMap.values()) {
            String[] words = hotel.getWords();
            String hotelName = hotel.getName();

            // store word frequencies for current document
            Map<String, Integer> _wordFrequencyMap = new HashMap<>();
            for (String w : words) {
                String word = w.toLowerCase();
                if (_wordFrequencyMap.containsKey(word)) {
                    // System.out.println("inside contains");
                    _wordFrequencyMap.put(word, _wordFrequencyMap.get(word) + 1);
                } else {
                    _wordFrequencyMap.put(word, 1);
                }
            }

            // put map _wordFrequencyMap with current document
            frequencyMapOfEachHotel.put(hotelName, _wordFrequencyMap);
        }
        // System.out.println(wordFrequency.toString());
    }

    public Map<String, Integer> calculateScores(String[] keywords, Set<String> hotelNamesSet) {
        // TODO: instead of map we should have used priority queue here.
        Map<String, Integer> documentScoreMap = new HashMap<>();
        for (String hotelName : hotelNamesSet) {
            Map<String, Integer> wordFrequencyMap = frequencyMapOfEachHotel.get(hotelName);
            int score = 0;
            // System.out.println(wordFrequencyMap.toString());
            for (String keyword : keywords) {
                if (wordFrequencyMap.containsKey(keyword)) {
                    score += wordFrequencyMap.get(keyword);
                    // score+=count;
                }
            }
            documentScoreMap.put(hotelName, score);
        }
        return documentScoreMap;
    }

    public void printIndex() {
        this.frequencyMapOfEachHotel.forEach((key, value) -> System.out.println(key + ":" + value));
    }
}
