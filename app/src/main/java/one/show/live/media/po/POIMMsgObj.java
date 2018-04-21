package one.show.live.media.po;



public class POIMMsgObj<T> extends POIMMsg {

    private T data;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}