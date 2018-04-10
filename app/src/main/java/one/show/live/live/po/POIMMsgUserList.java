package one.show.live.live.po;

import java.util.List;

/**
 * Created by apple on 16/6/8.
 */
public class POIMMsgUserList<T> extends POIMMsg {

    private List<T> data;


    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * 总人数
     */
    private int total;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
