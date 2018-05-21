package one.show.live.message.presenter;

import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import one.show.live.common.po.POListData;
import one.show.live.common.ui.BasePresenter;
import one.show.live.message.adapter.InteractionAdapter;
import one.show.live.message.binder.IInteractionBinder;
import one.show.live.message.mode.CommentListRequest;
import one.show.live.message.view.IInteractionView;
import one.show.live.personal.mode.AttentionOrCancelRequest;
import one.show.live.po.POAtMe;
import one.show.live.po.POComment;
import one.show.live.po.POPraise;
import one.show.live.po.eventbus.FocusOnEventBean;

/**
 * Created by clarkM1ss1on on 2018/5/4
 */
public class InteractionPresenter
        extends BasePresenter
        implements IInteractionBinder {

    private IInteractionView view;
    private InteractionAdapter adapter;

    private List<POComment> commentDataList;

    public InteractionPresenter(IInteractionView view) {
        this.view = view;
        adapter = new InteractionAdapter(this);
        commentDataList = new ArrayList<>();
    }

    public InteractionAdapter getAdapter() {
        return adapter;
    }

    public void requestToGetListData() {

        switch (view.getCurrentTag()) {
            case IInteractionView.TAG_AT_ME:
                requestAtMeData();
                break;
            case IInteractionView.TAG_PRAISE:
                requestPraiseData();
                break;
            default:
                //TODO get comment data
//                requestCommentData();
                break;
        }

    }

    private void requestPraiseData() {

    }

    private void requestCommentData(String uid, int cursor, int count) {
        new CommentListRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, POListData<POComment> data) {
                if (IInteractionView.TAG_COMMENTS == view.getCurrentTag()) {
                    commentDataList.addAll(data.getList());
                    adapter.notifyDataSetChanged();
                }
            }
        }.startRequest(uid, cursor, count);
    }

    private void requestAtMeData() {

    }

    public View.OnClickListener getFollowBtnOnClickListener(int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (v.isSelected()) {
//                    //TODO request to delete 'follow'
//                } else {
//                    //TODO request to add 'follow'
//                }
            }
        };
    }

    private void requestToFollow(final String uid, boolean isFollow) {

//        Map<String, String> map = new HashMap<>();
//        map.put("to_user_id", uid);
//        map.put("type", type);//添加关注
        new AttentionOrCancelRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, Object data) {
                if (isSuccess) {
                    EventBus.getDefault().post(new FocusOnEventBean(true, FocusOnEventBean.FOCUS_ON, msg, uid));//关注成功
                } else {
                    EventBus.getDefault().post(new FocusOnEventBean(false, FocusOnEventBean.FOCUS_ON, msg, ""));//关注失败
                }
            }
        }.changeFocus(uid, isFollow);

    }

    @Override
    public int getCurrentTag() {
        return 0;
    }

    @Override
    public List<POAtMe> getAtMeData() {
        return null;
    }

    @Override
    public List<POComment> getCommentData() {
        return null;
    }

    @Override
    public List<POPraise> getPraiseData() {
        return null;
    }
}
