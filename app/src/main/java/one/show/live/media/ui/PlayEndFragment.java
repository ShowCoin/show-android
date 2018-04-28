package one.show.live.media.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POLaunch;
import one.show.live.common.po.POLive;
import one.show.live.common.ui.BaseFragment;
import one.show.live.common.view.CustomDialogView;
import one.show.live.common.view.ToastUtils;
import one.show.live.media.po.POIMEnd;
import one.show.live.personal.persenter.MyWorkPresenter;
import one.show.live.personal.view.MyWorkView;
import one.show.live.po.POMaster;
import one.show.live.po.POShareMode;
import one.show.live.share.presenter.UpSharePresenter;
import one.show.live.share.ui.ShareLayout;
import one.show.live.util.EventBusKey;

/**
 * Created by Administrator on ..6/7 0007.
 * <p/>
 * 直播结束页面
 */
public class PlayEndFragment extends BaseFragment implements MyWorkView{

    @BindView(R.id.share_text)
    TextView shareText;
    @BindView(R.id.end_bg)
    SimpleDraweeView endBg;

    @BindView(R.id.like_number)
    TextView likeNumber;
    @BindView(R.id.share_layout)
    LinearLayout shareLayout;

    private POIMEnd endData;
    private POLive live;

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.audience_number)
    TextView audienceNum;
    @BindView(R.id.goldcoins_number)
    TextView coinsNum;
    @BindView(R.id.start_back)
    TextView startBack;
    @BindView(R.id.delete_replay)
    TextView deleteReplayVideo;

    private ShareLayout mshare;
    private UpSharePresenter upSharePresenter;
    private MyWorkPresenter deleteReplayPresenter;

    public static one.show.live.media.play.ui.PlayEndFragment getInstance(POIMEnd bean, POLive liveBean) {
        one.show.live.media.play.ui.PlayEndFragment endFragment = new one.show.live.media.play.ui.PlayEndFragment();
        endFragment.live = liveBean;
        endFragment.endData = bean;
        return endFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_end_layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        //setCover();
        upSharePresenter = new UpSharePresenter();
        deleteReplayPresenter = new MyWorkPresenter(this);

    }

    private void setCover() {
        //Postprocessor redMeshPostprocessor = new BasePostprocessor() {
        //  @Override public String getName() {
        //    return "Image Blur";
        //  }
        //
        //  @Override public void process(Bitmap bitmap) {
        //    ImageBlur.blurBitMap(bitmap, 10);
        //  }
        //};
        //
        //ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(live.getCover()))
        //    .setPostprocessor(redMeshPostprocessor)
        //    .build();
        //
        //PipelineDraweeController controller =
        //    (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
        //        .setImageRequest(request)
        //        .setOldController(videoCover.getController())
        //        .build();
        //videoCover.setController(controller);
//        videoCover.setImageURI(Uri.parse(live.getCover()));
    }

    @Override
    protected void initView() {
        super.initView();
        mshare = new ShareLayout(getContext(),live);
        shareLayout.addView(mshare);

        titleText.setText(endData.getMessageByReason());
        audienceNum.setText(getString(R.string.play_end_audience, endData.getViewed()));
        coinsNum.setText(getString(R.string.play_end_goldcoins, endData.getReceive()));
        likeNumber.setText(getString(R.string.play_end_like, endData.getLiked()));
        daDian("launch_end_event");//进入直播结束页的打点统计
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick({R.id.end_bg, R.id.start_back, R.id.delete_replay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.end_bg:
                break;
            case R.id.start_back:
                getActivity().finish();
                break;
            case R.id.delete_replay:
                CustomDialogView dialog = new CustomDialogView.Builder(getContext()).setTitle(getString(R.string.delete_playback))
                        .setLeftButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setRightButton(getString(R.string.determine), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                deleteReplayPresenter.deleteData(endData.getLiveID());
                            }
                        }).create();
                dialog.show();
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(POEventBus bean) {
        daDian("launch_end_share_event");//进入直播结束页的分享打点统计
        switch (bean.getId()) {
            case EventBusKey.EVENT_SHARE_BY_WEI_XIN:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_success);
                upSharePresenter.upShare(POShareMode.WX, endData.getLiveID());
                break;
            case EventBusKey.EVENT_SHARE_BY_WEIBO_SUCCESS:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_success);
                upSharePresenter.upShare(POShareMode.WB,endData.getLiveID());
                break;
            case EventBusKey.EVENT_SHARE_BY_WEIBO_FAILED:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_failed);
                break;
            case EventBusKey.EVENT_SHARE_BY_QQ_SUCCESS:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_success);
                upSharePresenter.upShare(POShareMode.QQ,endData.getLiveID());
                break;
            case EventBusKey.EVENT_SHARE_BY_QQ_FAILED:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_failed);
                break;
            case EventBusKey.EVENT_SHARE_BY_QQZONE_SUCCESS:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_success);
                upSharePresenter.upShare(POShareMode.QZ,endData.getLiveID());
                break;
            case EventBusKey.EVENT_SHARE_BY_QQZONE_FAILED:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_failed);
                break;
        }
    }

    @Override public void deleteSuccess() {
        deleteReplayVideo.setVisibility(View.INVISIBLE);
    }

    @Override public void onRefreshComplete(boolean isSuccess, POMaster poMaster) {

    }

    @Override public void onLoadMoreComplete(boolean isSuccess, POMaster poMaster) {

    }

    /**
     * 直播结束页面的打点统计
     */
    private void daDian(String even){
        Map<String,String> maps = new HashMap<>();
        maps.put(POLaunch.M_event,even);
        maps.put(POLaunch.M_tn,String.valueOf(++POLaunch.actionNum));
        maps.put(POLaunch.M_pname,"live_publish");
        maps.put(POLaunch.M_vid,endData.getLiveID());
        actionPresenter.actionUp(maps);
    }
}
