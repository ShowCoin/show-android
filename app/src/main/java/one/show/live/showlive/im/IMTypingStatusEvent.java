package one.show.live.im;

import java.util.Collection;

import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;

/**
 * Event for typing status changed  
 * Created by clarkM1ss1on on 2018/5/5
 */
public class IMTypingStatusEvent {
    private Conversation.ConversationType conversationType;
    private String targetId;
    private Collection<TypingStatus> statuses;

    public IMTypingStatusEvent(Conversation.ConversationType conversationType
            , String targetId
            , Collection<TypingStatus> statuses) {
        this.conversationType = conversationType;
        this.targetId = targetId;
        this.statuses = statuses;
    }

    public Conversation.ConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(Conversation.ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Collection<TypingStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(Collection<TypingStatus> statuses) {
        this.statuses = statuses;
    }
}
