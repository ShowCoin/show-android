package one.show.live.message.presenter;

import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import one.show.live.R;
import one.show.live.common.cache.MemberCacheManager;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BasePresenter;
import one.show.live.im.IMReceivePrivateMsgEvent;
import one.show.live.log.Logger;
import one.show.live.message.binder.IConversationListBinder;
import one.show.live.message.adapter.ConversationListAdapter;
import one.show.live.message.view.IConversationListView;
import one.show.live.personal.mode.UserRequest;

/**
 * Created by clarkM1ss1on on 2018/4/28
 */

public class ConversationListPresenter extends BasePresenter
        implements IConversationListBinder {

    private final static String TAG = "ConversationListPresenter";
    private IConversationListView view;
    private List<Conversation> cvData;
    private Set<String> doingRequestIds;

    private ConversationListAdapter adapter;
    private int mockIndex = 0;
    private static final String debugTargetId = "988687539361677312";

    public ConversationListPresenter(IConversationListView view) {
        initContext(view);
        this.view = view;
        doingRequestIds = new HashSet<>();
        adapter = new ConversationListAdapter(this);
    }

    public void registerEvents() {
        EventBus.getDefault()
                .register(this);
    }

    public void unregisterEvents() {
        EventBus.getDefault()
                .unregister(this);
    }

    public ConversationListAdapter getAdapter() {
        return adapter;
    }


    public void requestCvList() {
        RongIMClient.getInstance()
                .getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    @Override
                    public void onSuccess(List<Conversation> conversations) {
                        cvData = conversations;
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        //TODO show error toast
                        Logger.e(TAG, "onError");
                    }
                });
    }


    @Override
    public List<Conversation> getData() {
        return cvData;
    }

    @Override
    public View.OnClickListener getCvItemOnClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Conversation cv = cvData.get(position);
                view.moveToConversationView(cv.getTargetId());
            }
        };
    }

    @Override
    public View.OnClickListener getMessageExtraEntriesOnClickListener(int entries) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                view.moveToConversationView("989827593924640768");
                switch (v.getId()) {
                    case R.id.message_ex_entry_fans:
                        view.moveToFansView();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessageEvent(IMReceivePrivateMsgEvent event) {
        requestCvList();
    }

    @Override
    public View.OnClickListener getSendMsgOnClickListenerForDebug(String targetId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextMessage text = TextMessage
                        .obtain("msg sending test" + String.valueOf(mockIndex));
                mockIndex++;
                Message msg = Message
                        .obtain(debugTargetId, Conversation.ConversationType.PRIVATE, text);
                RongIMClient.getInstance()
                        .sendMessage(msg, null, null, new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {
                                Logger.e(TAG, "onAttached");
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
        };
    }

    @Override
    public View.OnClickListener getClearMsgOnClickListenerForDebug(String targetId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIMClient.getInstance()
                        .clearMessages(Conversation.ConversationType.PRIVATE, debugTargetId, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                Logger.e(TAG, "clear success");
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Logger.e(TAG, "clear onError");
                            }
                        });

            }
        };
    }

    @Override
    public POMember getTargetInfo(String targetId) {
        return MemberCacheManager.INSTANCE
                .getCachedById(targetId);
    }

    public View.OnClickListener getContractOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.moveToContract();
            }
        };
    }


    @Override
    public void requestToGetTargetInfo(final String uid) {
        final HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("uid", uid);
        doingRequestIds.add(uid);
        new UserRequest() {

            @Override
            public void onFinish(boolean isSuccess, String msg, POMember data) {
                if (isSuccess) {
                    MemberCacheManager.INSTANCE
                            .saveAsCache(data);
                    adapter.notifyDataSetChanged();
                }
                doingRequestIds.remove(uid);
            }
        }.getData(paramsMap);
    }

    @Override
    public boolean isTargetInfoRequesting(String targetId) {
        return doingRequestIds.contains(targetId);
    }
}
