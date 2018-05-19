package one.show.live.message.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.common.ui.BaseDialogFragment;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.message.binder.ILiveInnerBinder;
import one.show.live.message.presenter.ConversationListPresenter;
import one.show.live.message.view.IConversationListView;
import one.show.live.widget.TitleView;

/**
 * Created by clarkM1ss1on on 2018/5/16
 */
public class LiveInnerConversationListDialog
        extends BaseDialogFragment<ILiveInnerBinder> implements IConversationListView {

    public final static String TAG = "LiveInnerConversationListDialog";

    @BindView(R.id.messages_title)
    TitleView titleView;

    @BindView(R.id.messages_cv_list)
    RecyclerView messageListView;

    private ConversationListPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_conversation_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ConversationListPresenter(this);
        setupTitleView();
        setupMessageList();
        presenter.requestCvList();
    }

    @Override
    public void onStart() {
        super.onStart();
        setupWindow();
    }
    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setupWindow();
//        setContentView(R.layout.activity_conversation_list);
//        presenter = new ConversationListPresenter(this);
//        setupTitleView();
//        setupMessageList();
//        presenter.requestCvList();
//    }

    private void setupWindow() {
        final Window window = getDialog()
                .getWindow();
        int windowHeight = getResources()
                .getDimensionPixelSize(R.dimen.live_inner_conversation_window_height);
        final WindowManager.LayoutParams lp = window
                .getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = windowHeight;
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(null);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    private void setupTitleView() {
        titleView.setTitle(R.string.message);
        titleView.setLayBac(R.color.color_ebeaee);
        titleView.setTitleViewHeight(R.dimen.live_inner_title_height);
    }

    private void setupMessageList() {
        messageListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        messageListView.setAdapter(presenter.getAdapter());
    }


    @Override
    public void moveToConversationView(String targetId) {
//        startActivity(LiveInnerConversationActivity.getCallingBundle(this,targetId));
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(new LiveInnerConversationDialog(), "tagConversation")
//                .commit();
        mBinder.moveToConversationView(targetId);
    }

    @Override
    public void moveToFansView() {

    }

    @Override
    public void moveToContract() {

    }
}
