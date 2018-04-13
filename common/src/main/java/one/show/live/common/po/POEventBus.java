package one.show.live.common.po;

/**
 * 简单的 EventBus 对象
 */
public class POEventBus {
    private int id;
    private String data;

    public POEventBus(int id, String data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
