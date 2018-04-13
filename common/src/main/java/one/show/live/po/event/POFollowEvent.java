package one.show.live.po.event;

/**
 * 关注\取消关注的 EventBus
 *
 */
public class POFollowEvent {
    private long member;
    private int focus;

    public long getMember() {
        return member;
    }

    public void setMember(long member) {
        this.member = member;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }
}
