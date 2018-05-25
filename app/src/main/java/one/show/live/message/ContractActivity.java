package one.show.live.message.ui;

import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.message.view.IContractView;
import one.show.live.message.view.IConversationView;

/**
 * Created by clarkM1ss1on on 2018/5/4
 */
public class ContractActivity
        extends BaseFragmentActivity
        implements IContractView {

    @BindView(R.id.keywordEditText)
    EditText keywordEditView;

    @BindView(R.id.contractsList)
    RecyclerView contractsList;

    public static Intent getCallingIntent(Context ctx) {
        return new Intent(ctx, ContractActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
    }

    @Override
    public void moveBack() {

    }

    @Override
    public String getSearchingKeyWord() {
        return null;
    }

    @Override
    public void moveToConversationView(String targetId) {

    }
}
