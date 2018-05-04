package one.show.live.showlive.im;

/**
 * Event for {@link io.rong.imlib.model.Conversation.ConversationType#PRIVATE}.
 */
public class IMReceivePrivateMsgEvent {

    private int type;
    private Object data;

    public IMReceivePrivateMsgEvent(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
