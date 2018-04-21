package one.show.live.media.po;

import java.util.List;

public class POIMUserList<T> {
    private List<T> userList;   //数组列表
    /**
     * 下一页数据的起始下标
     */
    private int next_cursor;

    /**
     * 总人数
     */
    private int total;


    public int getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(int next_cursor) {
        this.next_cursor = next_cursor;
    }


    public List<T> getUserList() {
        return userList;
    }

    public void setUserList(List<T> userList) {
        this.userList = userList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * 是否还有下一页数据
     * @return
     */
    public boolean hasMore() {
        return next_cursor != -1;
    }
}
