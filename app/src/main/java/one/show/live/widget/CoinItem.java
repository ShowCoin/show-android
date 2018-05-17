package one.show.live.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.common.po.POMember;
import one.show.live.common.view.recycler.SimpleHolder;
import one.show.live.po.POTransactionRecords;
import one.show.live.po.POWithdrawal;

/**
 * Created by Nano on 2018/4/19.
 */

public class CoinItem extends SimpleHolder {
    Context context;
    @BindView(one.show.live.R.id.item_showdetailes_img)
    HeadImageView itemShowdetailesImg;
    @BindView(one.show.live.R.id.item_showdetailes_name)
    TextView itemShowdetailesName;
    @BindView(one.show.live.R.id.item_showdetailes_address)
    TextView itemShowdetailesAddress;
    @BindView(one.show.live.R.id.item_showdetailes_date)
    TextView itemShowdetailesDate;
    @BindView(one.show.live.R.id.item_showdetailes_num)
    TextView itemShowdetailesNum;


    public CoinItem(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(one.show.live.R.layout.item_showdetailes, parent, false));
    }

    public CoinItem(Context context, View view) {
        super(view);
        this.context = context;
        ButterKnife.bind(this, view);
    }

    /**
     * 添加数据  交易记录的
     */
    public void setData(POTransactionRecords poTransactionRecords) {

        if (poTransactionRecords == null) {
            return;
        }
        int r = poTransactionRecords.getShowNumber().compareTo(BigDecimal.ZERO);

        if (r == -1) {//小于0，是送出
            itemShowdetailesImg.setRightImg(one.show.live.R.drawable.payment_img);
            itemShowdetailesNum.setText(poTransactionRecords.getShowNumber() + " SHOW");
            itemShowdetailesAddress.setText(poTransactionRecords.getTo_address());
        } else if (r == 1) {//大于0  是收入
            itemShowdetailesNum.setText("+" + poTransactionRecords.getShowNumber() + " SHOW");
            itemShowdetailesImg.setRightImg(one.show.live.R.drawable.collection_img);
            itemShowdetailesAddress.setText(poTransactionRecords.getFrom_address());
        }

        itemShowdetailesImg.setImage(poTransactionRecords.getAvatar());
        itemShowdetailesName.setText(poTransactionRecords.getNickname());
        itemShowdetailesDate.setText(poTransactionRecords.getDate());

    }

    /**
     * 添加数据  提现记录的
     */
    public void setData(POWithdrawal poWithdrawal) {

        if (poWithdrawal == null) {
            return;
        }
        int ii = poWithdrawal.getShowNumber().indexOf(".");
        String ss;
        if(ii==-1){
            ss = poWithdrawal.getShowNumber();
        }else{
            ss =  poWithdrawal.getShowNumber().substring(0,ii);
        }


        itemShowdetailesImg.setRightImg(one.show.live.R.drawable.payment_img);
        itemShowdetailesNum.setText(ss + " SHOW");
        itemShowdetailesAddress.setText(poWithdrawal.getTo_address());

        itemShowdetailesImg.setImage(POMember.getInstance().getAvatar());
        itemShowdetailesName.setText(poWithdrawal.getName());
//        itemShowdetailesDate.setText(poTransactionRecords.getDate());
        itemShowdetailesDate.setText("2018年05月15日20:59:13暂时没有，等后台接口");

    }

}
