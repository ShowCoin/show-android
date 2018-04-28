package one.show.live.media.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POLaunch;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.StringUtils;
import one.show.live.common.util.UIUtils;
import one.show.live.common.util.UserInfoCacheManager;
import one.show.live.common.view.ToastUtils;
import one.show.live.media.po.POInitLive;
import one.show.live.media.po.POLiveStatus;
import one.show.live.media.presenter.ReadyToLivePresenter;
import one.show.live.media.presenter.UpSharePresenter;
import one.show.live.media.view.ReadyToLiveView;
import one.show.live.util.EventBusKey;
import one.show.live.util.NoDoubleClickListener;
import one.show.live.util.ToolUtil;

/**
 * Created by Nano on 2018/4/10.
 */

public class ReadyLiveActivity extends BaseFragmentActivity {


    @BindView(R.id.readylive_cha)
    ImageView readyliveCha;
    @BindView(R.id.readytolive_rotato)
    ImageView readytoliveRotato;
    @BindView(R.id.readytolive_title_context)
    EditText readytoliveTitleContext;
    @BindView(R.id.share_wxfriends)
    TextView shareWxfriends;
    @BindView(R.id.share_wx)
    TextView shareWx;
    @BindView(R.id.share_weibo)
    TextView shareWeibo;
    @BindView(R.id.readylive_center_lay)
    LinearLayout readyliveCenterLay;
    @BindView(R.id.readylive_button)
    TextView readyliveButton;
    @BindView(R.id.readytolive_meiyan)
    ImageView readytoliveMeiyan;
    @BindView(R.id.readytolive_positioning)
    TextView readytolivePositioning;


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, ReadyLiveActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_readylive;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
