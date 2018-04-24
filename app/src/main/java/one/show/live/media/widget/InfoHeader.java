package one.show.live.media.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import one.show.live.R;
import one.show.live.common.global.presenter.ActionPresenter;
import one.show.live.common.po.POLaunch;
import one.show.live.common.po.POLive;
import one.show.live.common.util.FrescoUtils;
import one.show.live.common.util.NumberUtil;
import one.show.live.common.util.TimeUtil;
import one.show.live.common.view.ToastUtils;
import one.show.live.personal.persenter.AttentionOrCancelPresenter;
import one.show.live.personal.view.AttentionOrCancelView;


/**
 * 自定义,直播时主播信息的显示
 */
public class InfoHeader extends RelativeLayout implements AttentionOrCancelView {
    private SimpleDraweeView headerIV;
    private ImageView headerVip;
    private TextView nameTV, numberTV;
    private Button followBtn;
    private String tagMsg, statusMsg;
    private String memberId;
    private long startTime;
    private String headVid;
    AttentionOrCancelPresenter attPresenter;
    ActionPresenter actionPresenter;

    public interface OnInfoClickListener {
        void clickHeader();
    }

    private OnInfoClickListener listener;

    public void setOnInfoClickListener(OnInfoClickListener listener) {
        this.listener = listener;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (!TextUtils.isEmpty(tagMsg)) {
                nameTV.setText(String.format("%s %s", tagMsg, TimeUtil.formatDuration(startTime)));
            } else {
                nameTV.setText(TimeUtil.formatDuration(startTime));
            }
            handler.sendEmptyMessageDelayed(0, 1000);
            return true;
        }
    });

    public InfoHeader(Context context) {
        super(context);
        init(context);
    }

    public InfoHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InfoHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        attPresenter = new AttentionOrCancelPresenter(this);
        actionPresenter = new ActionPresenter();
        LayoutInflater.from(context).inflate(R.layout.view_info_header, this);
        headerIV = (SimpleDraweeView) findViewById(R.id.header_iv);
        headerVip = (ImageView) findViewById(R.id.infoHeader_vip);


        headerIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.clickHeader();
                }
            }
        });
        nameTV = (TextView) findViewById(R.id.name_tv);
        numberTV = (TextView) findViewById(R.id.number_tv);
        followBtn = (Button) findViewById(R.id.follow_btn);
        followBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toFollowed();
            }
        });
    }

    /**
     * 设置用户头像
     *
     * @param url      头像地址
     * @param isVerify 是否认证用户
     */
    public void setAvatar(String url, boolean isVerify) {
        headerIV.setImageURI(FrescoUtils.getUri(url));
    }

    /**
     * 设定用户ID
     *
     * @param memberId 用户ID
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /**
     * 设置用户名称
     *
     * @param name 用户昵称
     */
    public void setName(String name) {
        nameTV.setText(name);
    }

    /**
     * 设置房间的vid
     * @param headVid
     */
    public void setVid(String headVid) {
        this.headVid = headVid;
    }


    /**
     * 设置观看中人数
     *
     * @param online 此时在线人数
     */
    public void setOnline(int online) {
        if (online <= 0)
            return;
        numberTV.setText(String.format("%s 人", online));
    }

  /**
   * 隐藏在线人数
   */
  public void hideOnlineNum(){
        numberTV.setVisibility(View.GONE);
    }

  /**
   * 显示在线人数
   */
  public void showOnlineNum(){
        numberTV.setVisibility(View.VISIBLE);
    }

    /**
     * 显示关注按钮
     *
     * @param isShow 是否显示关注按钮
     */
    public void showFollowBtn(boolean isShow) {
        followBtn.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setIsVip(boolean isVip) {
        headerVip.setVisibility(isVip ? VISIBLE : GONE);
    }

    /**
     * 关注用户
     */
    private void toFollowed() {
        attPresenter.saveFocus(memberId);
        daDian();//点击关注按钮的打点统计

    }

    /**
     * 根据liveBean初始化用户layout
     *
     * @param liveBean
     */
    public void initView(POLive liveBean) {

    }

    /**
     * 关注成功
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void focusSuccess(boolean isSuccess, String msg) {
        showFollowBtn(false);
                    //FIXME 发送事件
                    //                FollowEventBean bean = new FollowEventBean();
//                bean.setMember(memberId);
//                bean.setFocus(1);
//                EventBus.getDefault().post(bean);
    }

    /**
     * 关注失败
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void focusFailure(boolean isSuccess, String msg) {
        ToastUtils.showToast(msg);
    }

    /**
     * 取消关注成功
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void unFocusSuccess(boolean isSuccess, String msg) {

    }

    /**
     * 取消关注失败
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void unFocusFailure(boolean isSuccess, String msg) {

    }

    /**
     * 关注用户打点
     */
    public void daDian(){
        Map<String,String> maps = new HashMap<>();
        maps.put(POLaunch.M_event,"live_follow_btn_event");
        maps.put(POLaunch.M_tn,String.valueOf(++POLaunch.actionNum));
        maps.put(POLaunch.M_pname,"live_play");
        maps.put(POLaunch.M_vid,headVid);
        actionPresenter.actionUp(maps);
    }

}
