package one.show.live.message.adapter;

import android.view.ViewGroup;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import one.show.live.R;
import one.show.live.common.view.recycler.simple.SimpleAdapter;
import one.show.live.common.view.recycler.simple.SimpleHolder;
import one.show.live.message.binder.IConversationBinder;
import one.show.live.widget.ImageMessageItem;
import one.show.live.widget.TextMessageItem;
import one.show.live.widget.VoiceMessageItem;

/**
 * Created by clarkM1ss1on on 2018/4/28
 */
public class ConversationAdapter
        extends SimpleAdapter<IConversationBinder> {

    private final static String TAG = "ConversationAdapter";

    public ConversationAdapter(IConversationBinder binder) {
        super(binder);
    }

    private final int TYPE_UNKNOWN = 1 << 0;
    private final int TYPE_TEXT = 1 << 1;
    private final int TYPE_VOICE = 1 << 2;
    private final int TYPE_IMAGE = 1 << 3;

    private final int TYPE_DIRECTION_SENT = 1 << 4;
    private final int TYPE_DIRECTION_RECEIVED = 1 << 5;

    @Override
    public Object getItem(int type, int subPosition) {
        return mBinder
                .getData()
                .get(subPosition);
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if ((viewType & TYPE_TEXT) > 0) {
            if ((viewType & TYPE_DIRECTION_SENT) > 0) {
                return new TextMessageItem(mBinder, parent, R.layout.item_text_message_sent, false);
            } else {
                return new TextMessageItem(mBinder, parent, R.layout.item_text_message_received, true);
            }

        } else if ((viewType & TYPE_VOICE) > 0) {
            if ((viewType & TYPE_DIRECTION_SENT) > 0) {
                return new VoiceMessageItem(mBinder, parent, R.layout.item_voice_message_sent, false);
            } else {
                return new VoiceMessageItem(mBinder, parent, R.layout.item_voice_message_received, true);
            }

        } else if ((viewType & TYPE_IMAGE) > 0) {
            if ((viewType & TYPE_DIRECTION_SENT) > 0) {
                return new ImageMessageItem(mBinder, parent
                        , R.layout.item_image_message_sent, R.drawable.rc_ic_bubble_no_right, false);
            } else {
                return new ImageMessageItem(mBinder, parent
                        , R.layout.item_image_message_received, R.drawable.rc_ic_bubble_no_left, true);
            }
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        if (null == mBinder.getData()) {
            return 0;
        }
        return mBinder
                .getData()
                .size();
    }

    @Override
    public int getSubPosition(int type, int position) {
        return position;
    }

    @Override
    public int getPositionBySubPosition(int subPosition) {
        return subPosition;
    }

    @Override
    public int getItemViewType(int position) {
        final Message msg = mBinder.getData()
                .get(getSubPosition(0, position));
        int type;
        final MessageContent content = msg.getContent();
        if (content instanceof TextMessage) {
            type = TYPE_TEXT;
        } else if (content instanceof VoiceMessage) {
            type = TYPE_VOICE;
        } else if (content instanceof ImageMessage) {
            type = TYPE_IMAGE;
        } else {
            return TYPE_UNKNOWN;
        }
        if (msg.getMessageDirection() == Message.MessageDirection.SEND) {
            type |= TYPE_DIRECTION_SENT;
        } else {
            type |= TYPE_DIRECTION_RECEIVED;
        }
        return type;
    }
}

