package one.show.live.message.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.message.presenter.ConversationListPresenter;
import one.show.live.message.presenter.ConversationPresenter;
import one.show.live.message.view.IConversationListView;
import one.show.live.widget.TitleView;

/**
 * Created by clarkM1ss1on on 2018/5/16
 */
public class LiveInnerConversationListActivity
        extends BaseFragmentActivity implements IConversationListView {

    @BindView(R.id.messages_title)
    TitleView titleView;

    @BindView(R.id.messages_cv_list)
    RecyclerView messageListView;

    private ConversationListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindow();
        setContentView(R.layout.activity_conversation_list);
        presenter = new ConversationListPresenter(this);
        setupTitleView();
        setupMessageList();
        presenter.requestCvList();
    }

    private void setupWindow() {
        final Window window = getWindow();
        int windowHeight = getResources()
                .getDimensionPixelSize(R.dimen.live_inner_conversation_window_height);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, windowHeight);
        window.setGravity(Gravity.BOTTOM);
    }


    private void setupTitleView() {
        titleView.setTitle(R.string.message);
        titleView.setLayBac(R.color.color_ebeaee);
        titleView.setTitleViewHeight(R.dimen.live_inner_title_height);
    }

    private void setupMessageList() {
        messageListView.setLayoutManager(new LinearLayoutManager(this));
        messageListView.setAdapter(presenter.getAdapter());
    }


    @Override
    public void moveToConversationView(String targetId) {
        startActivity(LiveInnerConversationActivity.getCallingIntent(this,targetId));
    }

    @Override
    public void moveToFansView() {

    }

    @Override
    public void moveToContract() {

    }
}
