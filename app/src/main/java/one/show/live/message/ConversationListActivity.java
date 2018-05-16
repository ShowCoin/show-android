package one.show.live.message.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.view.recycler.DividerDecoration;
import one.show.live.message.presenter.ConversationListPresenter;
import one.show.live.message.view.IConversationListView;
import one.show.live.personal.ui.FansActivity;
import one.show.live.widget.TitleView;

/**
 * Created by clarkM1ss1on on 2018/4/28
 */
public class ConversationListActivity
        extends BaseFragmentActivity
        implements IConversationListView {

    @BindView(R.id.messages_title)
    TitleView messageTitleView;

    @BindView(R.id.messages_cv_list)
    RecyclerView messagesCvRecycler;

    private ConversationListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBarForLightTitle(Color.WHITE);
        setContentView(R.layout.activity_messages);
        initBaseView();
    }

    private void initBaseView() {
        presenter = new ConversationListPresenter(this);
        setupTitleView();
        setupRecyclerView();
        presenter.requestCvList();
    }

    private void setupTitleView() {
        messageTitleView
                .setTitle(R.string.message)
                .setTextColor(ContextCompat.getColor(this, R.color.black));
        messageTitleView.setLayBac(R.color.color_ffffff);
        messageTitleView.setLeftImage(R.drawable.back_black, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        messageTitleView.setRightText("联", presenter.getContractOnClickListener());
        messageTitleView.setRightTextColor(R.color.black);
    }

    private void setupRecyclerView() {
        messagesCvRecycler.setLayoutManager(new LinearLayoutManager(this));
        messagesCvRecycler.setAdapter(presenter.getAdapter());
        messagesCvRecycler.addItemDecoration(new DividerDecoration(this, R.drawable.shape_divider));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.registerEvents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unregisterEvents();
    }

    @Override
    public void moveToConversationView(String targetUid) {
        startActivity(ConversationActivity.getCallingIntent(this, targetUid));
    }

    @Override
    public void moveToFansView() {
        startActivity(FansActivity.getCallingIntent(this));
    }

    @Override
    public void moveToContract() {
        startActivity(ContractActivity.getCallingIntent(this));
    }
}
