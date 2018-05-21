package one.show.live.message.presenter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import one.show.live.R;
import one.show.live.audio.IVoicePlayerInfoListener;
import one.show.live.audio.IVoiceRecorderInfoListener;
import one.show.live.audio.VoicePlayer;
import one.show.live.audio.VoiceRecorder;
import one.show.live.common.cache.MemberCacheManager;
import one.show.live.common.po.POLogin;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BasePresenter;
import one.show.live.common.util.DeviceUtils;
import one.show.live.im.IMConnectStatusEvent;
import one.show.live.im.IMDelegate;
import one.show.live.im.IMReceivePrivateMsgEvent;
import one.show.live.im.IMTypingStatusEvent;
import one.show.live.im.MessageGenerator;
import one.show.live.log.Logger;
import one.show.live.message.adapter.ConversationAdapter;
import one.show.live.message.binder.IConversationBinder;
import one.show.live.message.view.IConversationView;
import one.show.live.personal.mode.UserRequest;
import one.show.live.util.AvatarAndPhotoUtil;

/**
 * Created by clarkM1ss1on on 2018/4/28
 */
public class ConversationPresenter
        extends BasePresenter
        implements IConversationBinder, IVoiceRecorderInfoListener, IVoicePlayerInfoListener {
    private final static String TAG = "ConversationPresenter";
    private IConversationView view;
    private ConversationAdapter adapter;
    private List<Message> data;
    private VoiceRecorder recorder;
    private VoicePlayer player;
    //    private String playingMsgUid;
    private final static int MINIMUM_DURING = 1000;
    private final static int COUNT_PER_PAGE = 20;
    private String playingVoiceMsgFile = null;
    private AvatarAndPhotoUtil avatarAndPhotoUtil;
    private POMember targetInfo;

    private HashMap<Integer, Integer> imgProgressProvider;


    public ConversationPresenter(IConversationView view) {
        initContext(view);
        this.view = view;
        adapter = new ConversationAdapter(this);
        recorder = new VoiceRecorder();
        player = new VoicePlayer();
        imgProgressProvider = new HashMap<>();
    }

    public void initRecorder() {
        recorder.attach(mContext, null, this);
    }

    public void releaseRecorder() {
        recorder.release();
    }

    public void initPlayer() {
        player.attach(mContext, this);
    }

    public void releasePlayer() {
        player.release();
    }

    public ConversationAdapter getAdapter() {
        return adapter;
    }

    public void registerEvents() {
        EventBus.getDefault()
                .register(this);
    }

    public void unregisterEvent() {
        EventBus.getDefault()
                .unregister(this);

    }

    public void getMessage() {

        final Bundle args = view
                .getArgIntent();
        final String targetId = args
                .getString(IConversationView.ARG_KEY_TARGET_UID);
        Logger.e(TAG, "request to get msg");
        RongIMClient.getInstance()
                .getHistoryMessages(Conversation.ConversationType.PRIVATE, targetId, -1, COUNT_PER_PAGE, new RongIMClient.ResultCallback<List<Message>>() {

                    @Override
                    public void onSuccess(List<Message> messages) {
                        data = messages;
                        adapter.notifyDataSetChanged();
                        view.scrollToBottom(false);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });
    }


    @Override
    public List<Message> getData() {
        return data;
    }

    @Override
    public View.OnClickListener getVoiceMsgOnClickListener(final int subPosition) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subPosition < data.size()
                        && data.get(subPosition).getContent() instanceof VoiceMessage) {
                    final Message msg = data
                            .get(subPosition);
                    final VoiceMessage voiceMsg = (VoiceMessage) msg
                            .getContent();
                    final String path = voiceMsg.getUri().getPath();
                    if (null != playingVoiceMsgFile
                            && playingVoiceMsgFile.equals(path)
                            && player.isPlaying()) {
                        player.stop();
                        playingVoiceMsgFile = null;
                    } else {
                        player.startWithRequestAudioFocus(path);
                        playingVoiceMsgFile = path;
                    }
                    final int pos = adapter
                            .getPositionBySubPosition(subPosition);
                    adapter.notifyItemChanged(pos);
                }
            }
        };
    }

    @Override
    public String getPlayingVoiceFilePath() {
        return playingVoiceMsgFile;
    }

    @Override
    public int getImageUploadingProgress(int messageId) {
        int position = adapter
                .getPositionBySubPosition(messageId);
        if (imgProgressProvider.containsKey(position)) {
            return imgProgressProvider.get(position);
        } else {
            return -1;
        }
    }

    @Override
    public View.OnClickListener getImageMsgOnClickListener(final Uri remote, final Uri local) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != local) {
                    view.moveToZoomView(v, local);
                } else if (null != remote) {
                    view.moveToZoomView(v, remote);
                }
            }
        };
    }

    @Override
    public POLogin getSelfInfo() {
        return POLogin.getInstance();
    }

    @Override
    public POMember getTargetInfo() {
        return targetInfo;
    }

    public View.OnClickListener getBottomBarFuncBtnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.voiceMsgSwitchBtn:
                        if (!v.isSelected()) {
                            switchToVoiceWithPermissionRequest();
                        } else {
                            view.switchToTypingMsgMode();
                        }
                        break;
//                    case R.id.emojiBtn:
//                        //TODO switch or pop up emoji keyboard
//                        break;

                    case R.id.moreBtn:
                        if (view.isBottomPanelVisible()) {
                            view.hideBottomPanel();
                        } else {
                            view.showBottomPanel();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessageEvent(IMReceivePrivateMsgEvent event) {
        final String targetId = view
                .getArgIntent()
                .getString(IConversationView.ARG_KEY_TARGET_UID);

        if (event.getData().getTargetId().equals(targetId)) {
            data.add(0, event.getData());
            adapter.notifyItemInserted(data.size());
            view.scrollToBottom(true);
        }
        //TODO do some alert like ring or vibrate to notice new message incoming event not this contract
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectionEvent(IMConnectStatusEvent event) {
        //TODO update connection status ui perform

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTypingStatusEvent(IMTypingStatusEvent event) {
        final String targetId = view
                .getArgIntent()
                .getString(IConversationView.ARG_KEY_TARGET_UID);
        if (Conversation.ConversationType.PRIVATE == event.getConversationType()
                && targetId.equals(event.getTargetId())) {
            if (event.getStatuses().size() > 0) {
                final TypingStatus status = event.getStatuses().iterator()
                        .next();
                if (status.getTypingContentType()
                        .equals(TextMessage.class.getAnnotation(MessageTag.class))) {
                    view.setTitleText(R.string.typing);
                } else {
                    view.setTitleText(R.string.recording);
                }
            } else {
                view.setTitleText(R.string.conversation);
            }
        }
    }

    public void requestTargetUserInfo() {
        final String targetId = view
                .getArgIntent()
                .getString(IConversationView.ARG_KEY_TARGET_UID);

        final HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("uid", targetId);
        new UserRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, POMember data) {
                if (isSuccess) {
                    view.updateTitleText(data.getNickname());
                    targetInfo = data;
                    MemberCacheManager
                            .INSTANCE
                            .saveAsCache(data);
                    adapter.notifyDataSetChanged();
                }
            }
        }.getData(paramsMap);
    }


    private void sendMsg(Message msg) {
        RongIMClient.getInstance()
                .sendMessage(msg, null, null, new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(final Message message) {
                        Logger.e(TAG, "onAttached");
                        view.clearTypeModeInput();
                        data.add(0, message);
                        adapter.notifyItemInserted(0);
                        view.scrollToBottom(true);
                    }

                    @Override
                    public void onSuccess(Message message) {
                        Logger.e(TAG, "onSuccess");
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        Logger.e(TAG, "onError");

                    }
                });
    }

    private void sendImageMsg(Message imageMsg) {
        RongIMClient.getInstance()
                .sendImageMessage(imageMsg, null, null, new RongIMClient.SendImageMessageCallback() {
                    private int position;

                    @Override
                    public void onAttached(Message message) {
                        Logger.e(TAG, "onAttached");
                        position = data.size();
                        data.add(0, message);
                        adapter.notifyItemInserted(0);
                        view.scrollToBottom(true);
                        imgProgressProvider.put(message.getMessageId(), 0);
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        Logger.e(TAG, "onError");
                    }

                    @Override
                    public void onSuccess(Message message) {
                        Logger.e(TAG, "onSuccess");
                        if (view.isContextAlive()) {
                            imgProgressProvider.remove(message.getMessageId());
                            if (data.size() > position + 1) {
                                adapter.notifyItemChanged(data.size() - position - 1);
                            } else {
                                adapter.notifyItemChanged(0);
                            }
                        }
                    }

                    @Override
                    public void onProgress(Message message, int i) {
                        Logger.e(TAG, "progress :" + i);
                        if (view.isContextAlive()) {
                            imgProgressProvider.put(message.getMessageId(), i);
                            if (data.size() > position + 1) {
                                adapter.notifyItemChanged(data.size() - position - 1);
                            } else {
                                adapter.notifyItemChanged(0);
                            }
                        }
                    }
                });
    }


    public View.OnClickListener getInputEditorOnClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DeviceUtils
                        .showSoftInput(mContext, v);
                view.hideBottomPanel();
            }
        };
    }

    public View.OnTouchListener getVoiceMsgOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.setVoiceInputStatus(IConversationView.VOICE_STATUS_ACTION_DOWN);
                        recorder.startWithAudioFocusRequest(mContext);
                        Logger.e(TAG, "ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        view.setVoiceInputStatus(IConversationView.VOICE_STATUS_ACTION_UP);
                        recorder.stop();
                        Logger.e(TAG, "ACTION_CANCEL");
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        recorder.cancel();
                        Logger.e(TAG, "ACTION_MOVE");
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
    }

    public TextView.OnEditorActionListener getOnEditorActionListener() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEND:
                        if (TextUtils.isEmpty(view.getTextMsg())) {
                            return false;
                        }
                        final String targetId = view
                                .getArgIntent()
                                .getString(IConversationView.ARG_KEY_TARGET_UID);
                        final Message msg = MessageGenerator.generateMessage(targetId
                                , Conversation.ConversationType.PRIVATE
                                , view.getTextMsg());
                        sendMsg(msg);
                        break;
                }
                return false;
            }
        };
    }

    public void switchToVoiceWithPermissionRequest() {
        AndPermission.with(mContext)
                .permission(Permission.RECORD_AUDIO)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        view.switchToVoiceMsgMode();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //TODO toast or dialog to alert the permission was denied
                    }
                })
                .start();
    }


    public KeyboardVisibilityEventListener getKeyboardVisibilityEventListener() {
        return new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen && adapter.getItemCount() > 0) {
                    view.scrollToBottom(true);
                }
                if (isOpen) {
                    view.hideBottomPanel();
                    final String targetId = view
                            .getArgIntent()
                            .getString(IConversationView.ARG_KEY_TARGET_UID);
                    IMDelegate.INSTANCE
                            .sendTextTypingStatus(targetId);
                }
//
                if (isOpen) {
                    view.adjustWindow();
                } else {
                    view.restoreWindow();
                }
            }
        };
    }


    // recorder startWithAudioFocusRequest
    @Override
    public void onRecorderStart(Uri voiceFile) {
        //TODO show recording status

    }

    // recorder cancel
    @Override
    public void onRecorderCanceled(Uri voiceFile) {

    }

    // recorder stop
    @Override
    public void onRecorderStop(Uri voiceFile, long duration) {

        final int durationSec = (int) (duration / 1000);

        if (duration < MINIMUM_DURING) {
            //TODO the duration is  not enough to sent
            //TODO remove the temp file
        } else {
            final String targetId = view
                    .getArgIntent()
                    .getString(IConversationView.ARG_KEY_TARGET_UID);
            final Message msg = MessageGenerator.generateMessage(targetId
                    , Conversation.ConversationType.PRIVATE, voiceFile, (int) duration);
            Logger.e(TAG, "on send :" + durationSec);
            sendMsg(msg);
        }

    }

    // recorder error
    @Override
    public void onRecorderError() {
        //TODO alert something was wrong
    }

    // player stop
    @Override
    public void onPlayerStop() {
        Logger.e(TAG, "onPlayerStop");
        playingVoiceMsgFile = null;
        adapter.notifyDataSetChanged();
    }

    // player error
    @Override
    public void onPlayerError() {
        Logger.e(TAG, "onPlayerError");
    }

    // player completion
    @Override
    public void onPlayerCompletion(MediaPlayer mp) {
        Logger.e(TAG, "onPlayerCompletion");
        playingVoiceMsgFile = null;
        adapter.notifyDataSetChanged();
    }

    public View.OnClickListener getImageEntryOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                avatarAndPhotoUtil.iosDialogButtomImage();
                view.popupPhotoActionSheet();
            }
        };
    }

    public void onGetPhotoResult(String path) {

        final File imageFile = new File(path);
        if (imageFile.exists()) {
            final Uri thumbnailUri = Uri
                    .fromFile(imageFile);
            final String targetId = view
                    .getArgIntent()
                    .getString(IConversationView.ARG_KEY_TARGET_UID);
            final Message msg = MessageGenerator.generateMessage(targetId
                    , Conversation.ConversationType.PRIVATE, thumbnailUri
                    , thumbnailUri, true);
            sendImageMsg(msg);
        }
    }


    public void resultDelivery(int requestCode, int resultCode, Intent data) {
    }

    private void requestToLoadMore() {
        if (null != data && data.size() > 0) {
            final String targetId = view
                    .getArgIntent()
                    .getString(IConversationView.ARG_KEY_TARGET_UID);
            final int startIndex = data.size() - 1;
            int oldestId = data.get(startIndex).getMessageId();
            RongIMClient.getInstance()
                    .getHistoryMessages(Conversation.ConversationType.PRIVATE, targetId, oldestId, COUNT_PER_PAGE, new RongIMClient.ResultCallback<List<Message>>() {
                        @Override
                        public void onSuccess(List<Message> messages) {
                            data.addAll(messages);
                            view.invokeRefreshComplete();
                            adapter.notifyItemRangeChanged(startIndex, COUNT_PER_PAGE);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            view.invokeRefreshComplete();
                        }
                    });
        } else {
            view.invokeRefreshComplete();
        }
    }

    public PtrHandler getPtrFrameHandler() {
        return new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestToLoadMore();
            }
        };
    }

    public void requestPermissionToGetCameraPhoto() {
        //TODO

        AndPermission.with(mContext)
                .permission(Permission.CAMERA)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
//                        view.switchToVoiceMsgMode();
                        view.moveToCamera();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //TODO toast or dialog to alert the permission was denied
                    }
                })
                .start();
    }

    public void requestPermissionToGetAlbumPhoto() {
//        AndPermission.with(mContext)
//                .permission(Permission)
//                .onGranted(new Action() {
//                    @Override
//                    public void onAction(List<String> permissions) {
//                        view.switchToVoiceMsgMode();
//                    }
//                })
//                .onDenied(new Action() {
//                    @Override
//                    public void onAction(List<String> permissions) {
//                        //TODO toast or dialog to alert the permission was denied
//                    }
//                })
//                .start();
    }

}
