package hotelprice;

import java.io.Serializable;

public class Hotel implements Serializable{
    private String price;
    private String location;
    private String score;
    private String url;
    private String name;
    private String pageData;
    private String[] words;

    public Hotel(String price, String location, String score, String name, String url, String[] words) {
        this.price = price;
        this.location = location;
        this.score = score;
        this.name = name;
        this.url = url;
        // this.pageData = pageData;
        this.words = words;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getScore() {
        return score;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getPageData() {
        return pageData;
    }

    public String[] getWords() {
        return words;
    }
}
