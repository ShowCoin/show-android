package one.show.live.media.ui;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POLive;
import one.show.live.common.ui.BaseFragment;
import one.show.live.common.view.ToastUtils;
import one.show.live.media.po.POIMEnd;
import one.show.live.po.POShareMode;
import one.show.live.share.presenter.UpSharePresenter;
import one.show.live.share.ui.ShareLayout;
import one.show.live.util.EventBusKey;

/**
 * Created by Administrator on ..6/7 0007.
 * 看播结束页面
 */
public class LookEndFragment extends BaseFragment {

    @BindView(R.id.share_text)
    TextView shareText;
    @BindView(R.id.end_bg)
    SimpleDraweeView endBg;

    @BindView(R.id.like_number)//喜欢人数
            TextView likeNumber;
    @BindView(R.id.share_layout)
    LinearLayout shareLayout;
    @BindView(R.id.click_layout)
    LinearLayout clickLayout;

    private POLive live;
    private POIMEnd imEnd;

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.audience_number)//观看人数
            TextView audienceNum;
    @BindView(R.id.goldcoins_number)//珍珠数
            TextView coinsNum;
    @BindView(R.id.start_back)
    TextView startBack;
    @BindView(R.id.delete_replay)
    TextView deleteReplayVideo;

    private ShareLayout mshare;

    private UpSharePresenter upSharePresenter;

    public static one.show.live.media.play.ui.LookEndFragment getInstance(POLive bean, POIMEnd imEnd) {
        one.show.live.media.play.ui.LookEndFragment endFragment = new one.show.live.media.play.ui.LookEndFragment();
        endFragment.live = bean;
        endFragment.imEnd = imEnd;
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
        setCover();
        EventBus.getDefault().register(this);
        upSharePresenter = new UpSharePresenter();
        coinsNum.setVisibility(View.GONE);
        deleteReplayVideo.setVisibility(View.GONE);
        startBack.setText(getResources().getString(R.string.determine));
        if (imEnd != null) {
            likeNumber.setText(String.format(getResources().getString(R.string.play_end_like), imEnd.getLiked()));
            audienceNum.setText(String.format(getResources().getString(R.string.play_end_audience), imEnd.getViewed()));
        }

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

    @OnClick({R.id.end_bg, R.id.title_text, R.id.audience_number, R.id.goldcoins_number, R.id.start_back, R.id.delete_replay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.end_bg:
                break;
            case R.id.title_text:

                break;
            case R.id.audience_number:

                break;
            case R.id.goldcoins_number:

                break;
            case R.id.start_back:
                //activity.onBackPressed();
                activity.finish();
                break;
            case R.id.delete_replay:

                break;
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(POEventBus bean) {
        switch (bean.getId()) {
            case EventBusKey.EVENT_SHARE_BY_WEI_XIN:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_success);
                upSharePresenter.upShare(POShareMode.WX,live.getVid());
                break;
            case EventBusKey.EVENT_SHARE_BY_WEIBO_SUCCESS:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_success);
                upSharePresenter.upShare(POShareMode.WB,live.getVid());
                break;
            case EventBusKey.EVENT_SHARE_BY_WEIBO_FAILED:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_failed);
                break;
            case EventBusKey.EVENT_SHARE_BY_QQ_SUCCESS:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_success);
                upSharePresenter.upShare(POShareMode.QQ,live.getVid());

                break;
            case EventBusKey.EVENT_SHARE_BY_QQ_FAILED:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_failed);
                break;
            case EventBusKey.EVENT_SHARE_BY_QQZONE_SUCCESS:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_success);
                upSharePresenter.upShare(POShareMode.QZ,live.getVid());
                break;
            case EventBusKey.EVENT_SHARE_BY_QQZONE_FAILED:
                ToastUtils.showToast(R.string.weibosdk_demo_toast_share_failed);
                break;
        }
    }
}
