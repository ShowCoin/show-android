package one.show.live.im;

import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.File;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import one.show.live.log.Logger;

/**
 * Created by clarkM1ss1on on 2018/5/7
 */
public class MessageGenerator {

    private final static String TAG = "MessageGenerator";

    /**
     * Generate message instance by message content
     *
     * @param targetId
     * @param conversationType
     * @param content
     * @return
     */
    public static Message generateMessage(String targetId
            , Conversation.ConversationType conversationType
            , MessageContent content) {
        return Message.obtain(targetId, conversationType, content);
    }


    /**
     * For text content message
     *
     * @param targetId
     * @param type
     * @param textContent
     * @return Message instance with content {@link TextMessage}
     */
    public static Message generateMessage(String targetId
            , Conversation.ConversationType type
            , String textContent) {
        final TextMessage txtMsg = TextMessage.obtain(textContent);
        return Message.obtain(targetId, type, txtMsg);
    }


    /**
     * For voice message
     *
     * @param targetId
     * @param type
     * @param audioFile Voice file uri
     * @param duration
     * @return Message instance with content {@link VoiceMessage}
     */
    @Nullable
    public static Message generateMessage(String targetId
            , Conversation.ConversationType type
            , Uri audioFile
            , int duration) {
        if (null != audioFile) {
            final File file = new File(audioFile.getPath());
            if (!file.exists() || file.length() == 0) {
                Logger.e(TAG, "generate voice message error, cause file 0 length or permission denied");
            }
            final VoiceMessage voiceMessage = VoiceMessage
                    .obtain(audioFile, duration);
            return generateMessage(targetId, type, voiceMessage);
        } else {
            return null;
        }
    }

    /**
     * For image message
     *
     * @param targetId
     * @param type
     * @param thumb    Thumbnail uri
     * @param local    Local image uri
     * @param isFull
     * @return Message instance with content {@link ImageMessage}
     */
    public static Message generateMessage(String targetId
            , Conversation.ConversationType type
            , Uri thumb
            , Uri local
            , boolean isFull) {
        final ImageMessage imageMessage = ImageMessage
                .obtain(thumb, local, isFull);
        return generateMessage(targetId, type, imageMessage);
    }

    /**
     *
     *
     * @param targetId
     * @param type
     * @param title
     * @param content
     * @param imageUrl
     * @return
     */
    public static Message generateMessage(String targetId, Conversation.ConversationType type
            , String title
            , String content
            , String imageUrl) {
        final RichContentMessage richContentMsg = RichContentMessage
                .obtain(title, content, imageUrl);
        return generateMessage(targetId, type, richContentMsg);
    }
}
