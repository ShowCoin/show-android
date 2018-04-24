package one.show.live.media.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.po.POLive;
import one.show.live.common.po.POMember;
import one.show.live.common.util.FixKitkatDialogUtils;
import one.show.live.common.util.FrescoUtils;
import one.show.live.common.util.StringUtils;
import one.show.live.common.view.ToastUtils;
import one.show.live.media.model.AdminRequest;
import one.show.live.media.model.BlockRequest;
import one.show.live.media.model.KickRequest;
import one.show.live.personal.model.FeedbackRequest;
import one.show.live.personal.model.MyPersonalRequest;
import one.show.live.personal.persenter.AttentionOrCancelPresenter;
import one.show.live.personal.view.AttentionOrCancelView;
import one.show.live.widget.ActionSheetDialog;

/**
 * Created by apple on 16/6/6.
 */
public class UserInfoDialog extends Dialog implements AttentionOrCancelView{

    @BindView(R.id.datadialog_cha)
    TextView datadialogCha;
    private OnUserInfoEventListener onUserInfoEventListener;

    AttentionOrCancelPresenter attPresenter;

    Dialog anchorSettingDialog,openReportChannelDialog,adminSettingDialog;

    public void mDismissDialog() {
        if(anchorSettingDialog!=null&&anchorSettingDialog.isShowing()){
            anchorSettingDialog.dismiss();
        }

        if(adminSettingDialog!=null&&adminSettingDialog.isShowing()){
            adminSettingDialog.dismiss();
        }

        if(openReportChannelDialog!=null&&openReportChannelDialog.isShowing()){
            openReportChannelDialog.dismiss();
        }

        if(isShowing()) {
            dismiss();
        }
    }

    public interface OnUserInfoEventListener {
        void onClickSendGift(POMember user);

        void onClickMessage(POMember user);

        void onClickReply(POMember user);

        void gotoUserInfo(POMember user);

        void gotoAdminList();
    }

    public void setOnUserInfoEventListener(OnUserInfoEventListener listener) {
        this.onUserInfoEventListener = listener;
    }

    private Context context;

    public static final int anchor = 1;
    public static final int admin = 2;
    public static final int user = 3;

    private int type = anchor;

    private POLive live;
    private POMember userInfo;

    @BindView(R.id.datadialog_set)
    TextView setting;
    @BindView(R.id.datadialog_report)
    TextView report;
    @BindView(R.id.datadialog_head)
    SimpleDraweeView headImg;
    @BindView(R.id.datadialog_vip)
    ImageView headImgVip;

    @BindView(R.id.datadialog_head_two)
    SimpleDraweeView rankImg;

    @BindView(R.id.datadialog_nickname)
    TextView nickName;

    @BindView(R.id.user_level)
    ImageView userLevel;

    @BindView(R.id.datadialog_id)
    TextView popularNo;
    @BindView(R.id.datadialog_intro)
    TextView desc;

    @BindView(R.id.datadialog_focus_num)
    TextView focusNum;
    @BindView(R.id.datadialog_fans_num)
    TextView fansNum;
    @BindView(R.id.datadialog_preal_num)
    TextView prealNum;

    @BindView(R.id.datadialog_focus)
    TextView toFocus;
    @BindView(R.id.datadialog_messages)
    TextView toMessage;

    @BindView(R.id.datadialog_reply)
    TextView toReply;

    public UserInfoDialog(Context context, int type) {
        super(context, one.show.live.common.R.style.MyDialogStyle);
        this.type = type;
        initView(context);
    }

    public void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.userinfo_dialog, null);
        setCanceledOnTouchOutside(true);
//        FixKitkatDialogUtils.fixDialogPre(this);
        setContentView(view);
        ButterKnife.bind(this);
        attPresenter = new AttentionOrCancelPresenter(this);
        if (isAnchor() || isAdmin()) {
            setting.setText("设置");
        } else {
            setting.setText("主页");
        }
    }

    private boolean isAnchor() {
        return type == anchor;
    }

    private boolean isAdmin() {
        return type == admin;
    }

    public void initData(String userId, POLive live) {
        this.live = live;
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        new MyPersonalRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, POMember data) {
                if (isSuccess) {
                    setData(data);
                } else {
                    ToastUtils.showToast("个人信息获取失败");
                }
            }
        }.startRequest(map);
    }

//    @Override
//    public void show() {
//        super.show();
//        FixKitkatDialogUtils.fixDialogPost(this);
//    }

    private void setData(POMember data) {
        userInfo = data;
        if (userInfo != null) {
            headImg.setImageURI(FrescoUtils.getUri(data.getProfileImg()));


            if(data.isVip()){
                headImgVip.setVisibility(View.VISIBLE);
            }else{
                headImgVip.setVisibility(View.GONE);
            }

            if (data.getDefendUserImgs() != null && data.getDefendUserImgs().size() > 0&& StringUtils.isNotEmpty(data.getDefendUserImgs().get(0))) {
                rankImg.setVisibility(View.VISIBLE);
                rankImg.setImageURI(FrescoUtils.getUri(data.getDefendUserImgs().get(0)));
            } else {
                rankImg.setVisibility(View.GONE);
            }

            nickName.setText(data.getBeke_nickname());
            if(data.getFanLevel()>0){
                userLevel.setVisibility(View.VISIBLE);
                userLevel.setImageResource(context.getResources().getIdentifier("level_"+data.getFanLevel(), "drawable", "tv.beke"));
            }else{
                userLevel.setVisibility(View.GONE);
            }

            popularNo.setText(String.format("ID:%s", data.getPopularNo()));
            desc.setText(data.getDescription());
            focusNum.setText(String.valueOf(data.getFollow()));
            fansNum.setText(String.valueOf(data.getFans()));
            prealNum.setText(String.valueOf(data.getReceive()));
            toFocus.setText(data.getIsFollowed() ? "已关注" : "+关注");
        }
    }

    @OnClick(R.id.datadialog_set)
    protected void clickSetting() {
        if (userInfo != null) {
            if (isAnchor()) {
                showAnchorSettingDialog();
            } else if (isAdmin()) {
                showAdminSettingDialog();
            } else {
                if (onUserInfoEventListener != null) {
                    onUserInfoEventListener.gotoUserInfo(userInfo);
                }
            }
        }
    }

    @OnClick(R.id.datadialog_head)
    protected void clickHead() {
        if (userInfo != null) {
            if (onUserInfoEventListener != null) {
                onUserInfoEventListener.gotoUserInfo(userInfo);
            }
        }
    }

    /**
     * 显示主播设置权限界面
     */
    private void showAnchorSettingDialog() {
        if (userInfo != null && !userInfo.getBeke_userid().equals(POMember.getInstance().getBeke_userid())) {
            anchorSettingDialog = new ActionSheetDialog(getContext())
//                    .setIsFixFullScreen(true)
                    .builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("管理员列表", ActionSheetDialog.SheetItemColor.Blick,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    if (onUserInfoEventListener != null) {
                                        onUserInfoEventListener.gotoAdminList();
                                    }
                                }
                            },false)
                    .addSheetItem(userInfo.isMyAdmin()?"取消管理":"设置管理", ActionSheetDialog.SheetItemColor.Blick,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("tid", userInfo.getBeke_userid());
                                    params.put("action", userInfo.isMyAdmin()?"0":"1");
                                    new AdminRequest() {
                                        @Override
                                        public void onFinish(boolean isSuccess, String msg, Object data) {
                                            if (isSuccess) {
                                                ToastUtils.showToast(userInfo.isMyAdmin()?"撤销管理员成功":"设置管理员成功");
                                                userInfo.setIs_my_admin(userInfo.isMyAdmin()?0:1);
                                            } else {
                                                ToastUtils.showToast(userInfo.isMyAdmin()?"撤销管理员失败":"设置管理员失败");
                                            }
                                        }
                                    }.startRequest(params);
                                }
                            })
                    .addSheetItem("禁言", ActionSheetDialog.SheetItemColor.Blick,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    blockRequest();
                                }
                            })
                    .addSheetItem("踢人", ActionSheetDialog.SheetItemColor.Blick,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    kickRequest();
                                }
                            }).show();
        }
    }


    /**
     * 设置管理员设置权限界面
     */
    private void showAdminSettingDialog() {
        adminSettingDialog = new ActionSheetDialog(getContext())
//                .setIsFixFullScreen(true)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("禁言", ActionSheetDialog.SheetItemColor.Blick,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                blockRequest();
                            }
                        })
                .addSheetItem("踢人", ActionSheetDialog.SheetItemColor.Blick,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                kickRequest();
                            }
                        }).show();
    }


    private void blockRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", userInfo.getBeke_userid());
        params.put("minute", "5");
        params.put("roomId", live.getRoom_id());
        new BlockRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, Object data) {
                if (isSuccess) {
                    ToastUtils.showToast("禁言成功");
                } else {
                    if(StringUtils.isNotEmpty(msg)){
                        ToastUtils.showToast(msg);
                    }else{
                        ToastUtils.showToast("禁言失败");
                    }
                }
            }
        }.startRequest(params);
    }

    private void kickRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", userInfo.getBeke_userid());
        params.put("minute", "60");
        params.put("roomId", live.getRoom_id());
        new KickRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, Object data) {
                if (isSuccess) {
                    ToastUtils.showToast("踢出成功");
                } else {
                    if(StringUtils.isNotEmpty(msg)){
                        ToastUtils.showToast(msg);
                    }else{
                        ToastUtils.showToast("踢出失败");
                    }
                }
            }
        }.startRequest(params);
    }

    /**
     * 举报
     */
    @OnClick(R.id.datadialog_report)
    protected void openReportChannel() {
        if (userInfo != null) {

            openReportChannelDialog  =   new ActionSheetDialog(getContext())
//                    .setIsFixFullScreen(true)
                    .builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("危险言论", ActionSheetDialog.SheetItemColor.Blick,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    dismiss();
                                    report(1);
                                }
                            })
                    .addSheetItem("侮辱谩骂", ActionSheetDialog.SheetItemColor.Blick,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    report(2);
                                }
                            })
                    .addSheetItem("色情", ActionSheetDialog.SheetItemColor.Blick,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    report(3);
                                }
                            })
                    .addSheetItem("广告", ActionSheetDialog.SheetItemColor.Blick,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    report(4);
                                }
                            }).show();

        }
    }


    private void report(int category) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userInfo.getBeke_userid());
        params.put("type", "1");
        params.put("category", category + "");

        new FeedbackRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, Object data) {
                if (isSuccess) {
                    ToastUtils.showToast("举报成功");
                } else {
                    ToastUtils.showToast("举报失败");
                }
            }
        }.startRequest(params);

    }


    /**
     * 关注用户
     */
    @OnClick(R.id.datadialog_focus)
    protected void focusUser() {
        if (userInfo != null && !userInfo.getIsFollowed()) {
            attPresenter.saveFocus(userInfo.getBeke_userid());
        }
    }

    @OnClick(R.id.datadialog_messages)
    protected void sendMessage() {
        if (userInfo != null) {
            dismiss();
            if (onUserInfoEventListener != null) {
                onUserInfoEventListener.onClickMessage(userInfo);
            }
        }
    }

    /**
     * 送礼
     */
//    @OnClick(R.id.datadialog_gift)
//    protected void sendGift() {
//        if(userInfo!=null) {
//            dismiss();
//            if (onUserInfoEventListener != null) {
//                onUserInfoEventListener.onClickSendGift(userInfo);
//            }
//        }
//    }

    /**
     * 右上角的X
     */
    @OnClick(R.id.datadialog_cha)
    public void onClick() {
        dismiss();
    }


    /**
     * 回复
     */
    @OnClick(R.id.datadialog_reply)
    protected void toReply() {
        if (userInfo != null) {
            dismiss();
            if (onUserInfoEventListener != null) {
                onUserInfoEventListener.onClickReply(userInfo);
            }
        }
    }

    /**
     * 关注成功
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void focusSuccess(boolean isSuccess, String msg) {
        if (UserInfoDialog.this.isShowing()) {
            userInfo.setIsFollowed(true);
            toFocus.setText("已关注");
        }
        ToastUtils.showToast("关注成功");
    }

    /**
     * 关注失败
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void focusFailure(boolean isSuccess, String msg) {

        if(StringUtils.isNotEmpty(msg)) {
            ToastUtils.showToast(msg);
        }else{
            ToastUtils.showToast("关注失败");
        }
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

}