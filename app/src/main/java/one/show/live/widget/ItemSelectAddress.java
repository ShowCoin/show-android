package one.show.live.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.R;
import one.show.live.po.POAddress;
import one.show.live.po.POAddressList;

/**
 * Created by Nano on 2018/4/4.
 */

public class ItemSelectAddress extends LinearLayout {
    Context context;
    @BindView(R.id.item_select_address_name)
    TextView itemSelectAddressName;

    @BindView(R.id.item_select_address_address)
    TextView itemSelectAddressAddress;

    @BindView(R.id.item_select_address_certification)
    TextView itemSelectAddressCertification;

    public ItemSelectAddress(Context context) {
        super(context);
        initView(context);
    }

    public ItemSelectAddress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.item_select_address, this);
        ButterKnife.bind(this);
    }

    public void setData(POAddress poAddress){
        itemSelectAddressName.setText(poAddress.getName());
        itemSelectAddressAddress.setText(poAddress.getAddress());
        if(poAddress.isCertification()){
            itemSelectAddressCertification.setText(context.getString(R.string.certification));
        }else{
            itemSelectAddressCertification.setText(context.getString(R.string.un_certification));
        }




    }

}
