package hotelprice;

public class SearchedQueryFrequency implements Comparable<SearchedQueryFrequency> {
    private String query;
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public String getQuery() {
        return query;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public SearchedQueryFrequency(String query, Integer count) {
        this.query = query;
        this.count = count;
    }
    
    @Override
    public int compareTo(SearchedQueryFrequency o) {
        return this.getQuery().compareTo(o.getQuery());
    }

}

    