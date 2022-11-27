package hotelprice;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class PageRank {
    
    PriorityQueue<Page> priorityQueue = new PriorityQueue<Page>(10, new PageComparator());;
    Map<String, Integer> hotelNameScoreMap;

    public PageRank(Map<String, Integer> documentScoreMap) {
        this.hotelNameScoreMap = documentScoreMap;
    }

    public void rankPages() {
        for (String hotelName: hotelNameScoreMap.keySet()) {
            int score = hotelNameScoreMap.get(hotelName);
            Page page = new Page(hotelName, score);
            priorityQueue.add(page);
        }
    }

    public List<String> getTopKHotels(int k) {
        List<String> list = new LinkedList<>();
        for (int i=0;i<k;i++){
            if (priorityQueue.isEmpty()) {
                break;
            }
            list.add(priorityQueue.remove().hotelName);
        }
        return list;
    }
}

class Page {
    public int score;
    public String hotelName;
    public Page(String hotelName, int score) {
        this.score = score;
        this.hotelName = hotelName;
    }
}

class PageComparator implements Comparator<Page>{
    public int compare(Page page1, Page page2) {
        if (page1.score < page2.score)
            return 1;
        else if (page1.score > page2.score)
            return -1;
        return 0;
    }
}