package helloworld.example.com.payback;

/**
 * Created by Owner on 2016-06-22.
 */
class TransItem {
    int cost;
    String name;
    String date;

    TransItem() {
        super();
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getCost() {
        return cost;
    }
    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
}