package one.show.live.live.po;


import java.util.List;

public  class POIMMsgList<T> extends POIMMsg {

    private List<T> data;


    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
