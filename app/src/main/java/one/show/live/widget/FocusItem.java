package one.show.live.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.R;
import one.show.live.po.POFocus;
import one.show.live.util.DeviceUtils;
import one.show.live.util.FrescoUtils;
import one.show.live.view.recycler.SimpleHolder;

public class FocusItem extends SimpleHolder {

    Context context;
    int sizeW;
    int sizeH;

    int size_50;
    int size_5;
    @BindView(R.id.focus_item_image)
    SimpleDraweeView focusImage;
    @BindView(R.id.focus_item_lay)
    RelativeLayout focusLay;

    int num;//一行显示num列
    @BindView(R.id.focus_item_header)
    SimpleDraweeView focusItemHeader;
    @BindView(R.id.focus_item_nickname)
    TextView focusItemNickname;
    @BindView(R.id.focus_item_bottom_lay)
    LinearLayout focusItemBottomLay;
    @BindView(R.id.focus_item_right_lay)
    LinearLayout focusItemRightLay;
    @BindView(R.id.focus_item_title)
    TextView focusItemTitle;

    public FocusItem(Context context, ViewGroup parent, int num) {
        this(context, LayoutInflater.from(context).inflate(R.layout.focus_item, parent, false), num);
    }

    public FocusItem(Context context, View view, int num) {
        super(view);
        this.context = context;
        ButterKnife.bind(this, view);
        sizeW = (DeviceUtils.getScreenWidth(context) - DeviceUtils.dipToPX(context, 1)) / num;
        if (num == 2) {
            sizeH = (sizeW * 16) / 10;  //显示2行的话16：10
            focusItemRightLay.setVisibility(View.VISIBLE);
            focusItemTitle.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DeviceUtils.dipToPX(context,20),DeviceUtils.dipToPX(context,20));
            focusItemHeader.setLayoutParams(lp);
            focusItemNickname.setTextSize(12);
        } else {
            sizeH = (sizeW * 4) / 3;  //显示3行的话4：3
            focusItemRightLay.setVisibility(View.GONE);
            focusItemTitle.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DeviceUtils.dipToPX(context,15),DeviceUtils.dipToPX(context,15));
            focusItemHeader.setLayoutParams(lp);
            focusItemNickname.setTextSize(10);
        }

        focusLay.setLayoutParams(new RelativeLayout.LayoutParams(sizeW, sizeH));
        size_50 = DeviceUtils.dipToPX(context, 50);
        size_5 = DeviceUtils.dipToPX(context, 5);
    }


    public void setData(final POFocus data) {
        if (data == null) {
            return;
        }

        FrescoUtils.bind(focusImage, data.getNickName(), sizeW, sizeH);
    }

}
