package one.show.live.message.ui;

import android.os.Bundle;

import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.message.view.IConversationListView;

/**
 * Created by clarkM1ss1on on 2018/5/16
 */
public class LiveInnerConversationListActivity
        extends BaseFragmentActivity implements IConversationListView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_inner_conversation_list);
    }

    @Override
    public void moveToConversationView(String targetUid) {

    }

    @Override
    public void moveToFansView() {

    }

    @Override
    public void moveToContract() {

    }
}
