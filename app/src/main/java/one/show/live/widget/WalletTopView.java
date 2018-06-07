package one.show.live.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.po.POLogin;
import one.show.live.common.po.POMember;
import one.show.live.common.util.Constants;
import one.show.live.common.view.ToastUtils;
import one.show.live.log.Logger;
import one.show.live.po.POWallet;
import one.show.live.util.CheckOnDoubleClickUtils;
import one.show.live.util.NoDoubleClickListener;
import one.show.live.wallet.view.WalletView;

/**
 * Created by Nano on Show.
 */

public class WalletTopView extends LinearLayout {
    Context context;
    @BindView(R.id.wallet_view_top)
    WalletTopHeadView walletViewTop;
    @BindView(R.id.wallet_view_center_left_show)
    TextView walletViewCenterLeftShow;
    @BindView(R.id.wallet_view_center_left_eth)
    TextView walletViewCenterLeftEth;
    @BindView(R.id.wallet_view_center_left_rmb)
    TextView walletViewCenterLeftRmb;
    @BindView(R.id.wallet_view_center_left_lay)
    LinearLayout walletViewCenterLeftLay;
    @BindView(R.id.wallet_view_center_center_show)
    TextView walletViewCenterCenterShow;
    @BindView(R.id.wallet_view_center_center_eth)
    TextView walletViewCenterCenterEth;
    @BindView(R.id.wallet_view_center_center_rmb)
    TextView walletViewCenterCenterRmb;
    @BindView(R.id.wallet_view_center_center_lay)
    LinearLayout walletViewCenterCenterLay;
    @BindView(R.id.wallet_view_center_right_show)
    TextView walletViewCenterRightShow;
    @BindView(R.id.wallet_view_center_right_eth)
    TextView walletViewCenterRightEth;
    @BindView(R.id.wallet_view_center_right_rmb)
    TextView walletViewCenterRightRmb;
    @BindView(R.id.wallet_view_center_right_lay)
    LinearLayout walletViewCenterRightLay;
    @BindView(R.id.wallet_view_exchange_rate)
    TextView walletViewExchangeRate;


    WalletView walletView;
    POMember poMember;
    @BindView(R.id.wallet_view_center_lay)
    LinearLayout walletViewCenterLay;
    @BindView(R.id.wallet_view_exchange_rate_lay)
    LinearLayout walletViewExchangeRateLay;

    @BindView(R.id.wallet_view_ok)
    TextView walletViewOk;
    @BindView(R.id.wallet_view_agreement)
    TextView walletViewAgreement;

    Map<String, String> masterMap;

    @BindView(R.id.wallet_view_center_left_bg)
    ImageView walletViewCenterLeftBg;
    @BindView(R.id.wallet_view_center_center_bg)
    ImageView walletViewCenterCenterBg;
    @BindView(R.id.wallet_view_center_right_bg)
    ImageView walletViewCenterRightBg;

    POWallet mPoWallet;

    public WalletTopView(Context context) {
        super(context);

        initView(context);
    }

    public WalletTopView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_wallet, this);
        ButterKnife.bind(this);


    }

    public void setViews(WalletView walletView, POMember poMember) {
        this.walletView = walletView;
        this.poMember = poMember;

        walletViewTop.setPOmemberData(poMember);

        if (POLogin.isCurrentUser(poMember.getUid())) {
            walletViewCenterLay.setVisibility(VISIBLE);
            walletViewExchangeRateLay.setVisibility(VISIBLE);
        } else {
            walletViewCenterLay.setVisibility(GONE);
            walletViewExchangeRateLay.setVisibility(GONE);
        }
    }

    /**
     * 添加数据
     *
     * @param poWallet
     */
    public void setWalletData(final POWallet poWallet) {
        if (poWallet == null) {
            return;
        }

        mPoWallet = poWallet;
        TextView showS[] = new TextView[]{walletViewCenterLeftShow, walletViewCenterCenterShow, walletViewCenterRightShow};
        TextView eths[] = new TextView[]{walletViewCenterLeftEth, walletViewCenterCenterEth, walletViewCenterRightEth};
        TextView rmbs[] = new TextView[]{walletViewCenterLeftRmb, walletViewCenterCenterRmb, walletViewCenterRightRmb};
        LinearLayout lays[] = new LinearLayout[]{walletViewCenterLeftLay, walletViewCenterCenterLay, walletViewCenterRightLay};
        ImageView imgs[] = new ImageView[]{walletViewCenterLeftBg, walletViewCenterCenterBg, walletViewCenterRightBg};

        walletViewTop.setTopData(poWallet.getShow_balance(), poWallet.getRmb_num(), Constants.SHOW_COIN, 1);




        final List<POWallet.PayConfigBean> pwpclist = poWallet.getPay_config();
        for (int i = 0; i < pwpclist.size(); i++) {

            if(i==0){//设置第一条为默认值
                setBg(i);
                Map<String, String> map = new HashMap<>();
                map.put("show_number", pwpclist.get(i).getShow_number() + "");
                map.put("eth_number", pwpclist.get(i).getEth_number() + "");
                masterMap = map;
            }

            showS[i].setText(pwpclist.get(i).getShow_number() + "");
            eths[i].setText(pwpclist.get(i).getEth_number() + " ETH");
            rmbs[i].setText(pwpclist.get(i).getRmb_number() + " CNY");
            final int finalI = i;
            lays[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setBg(finalI);
                    Map<String, String> map = new HashMap<>();
                    map.put("show_number", pwpclist.get(finalI).getShow_number() + "");
                    map.put("eth_number", pwpclist.get(finalI).getEth_number() + "");
                    masterMap = map;
                }
            });
        }
        walletViewExchangeRate.setText(poWallet.getEth_rate());
    }



    public void setBg(int id){

        walletViewCenterLeftBg.setBackgroundResource(R.color.transparent);
        walletViewCenterCenterBg.setBackgroundResource(R.color.transparent);
        walletViewCenterRightBg.setBackgroundResource(R.color.transparent);

        switch (id){
            case 0:
                walletViewCenterLeftBg.setBackgroundResource(R.drawable.color_5034b6ff_5);
                break;
            case 1:
                walletViewCenterCenterBg.setBackgroundResource(R.drawable.color_5034b6ff_5);
                break;
            case 2:
                walletViewCenterRightBg.setBackgroundResource(R.drawable.color_5034b6ff_5);
                break;
        }

    }

    @OnClick({R.id.wallet_view_ok, R.id.wallet_view_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wallet_view_ok://确认充值

                CheckOnDoubleClickUtils.onClick(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mPoWallet.getEth_balance().compareTo(new BigDecimal(masterMap.get("eth_number"))) < 0) {//如果eth不足就不进行下一步
                            ToastUtils.showToast("eth余额不足");
                            return;
                        }

                        if (walletView != null) {
                            walletView.onSelected(masterMap);
                        }
                    }
                });



                break;
            case R.id.wallet_view_agreement:
                break;
        }
    }
}
