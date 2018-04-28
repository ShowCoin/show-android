package one.show.live.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.R;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.util.FrescoUtils;
import one.show.live.common.view.recycler.SimpleHolder;
import one.show.live.po.POFocus;

/**
 * Created by Nano on 2017/11/15.
 */

public class HotItem extends SimpleHolder {

    Context context;
    @BindView(R.id.hot_item_img)
    SimpleDraweeView hotItemImg;
    @BindView(R.id.hot_item_nickname)
    TextView hotItemNickname;
    @BindView(R.id.hot_item_header)
    SimpleDraweeView hotItemHeader;
    @BindView(R.id.hot_item_watch)
    TextView hotItemWatch;
    @BindView(R.id.hot_item_right_lay)
    LinearLayout hotItemRightLay;
    @BindView(R.id.hot_item_title)
    TextView hotItemTitle;
    @BindView(R.id.hot_item_city)
    TextView hotItemCity;

    int size60;


    public HotItem(Context context, ViewGroup parent) {

        this(LayoutInflater.from(context).inflate(R.layout.hot_item, parent, false), context);
    }

    public HotItem(View itemView, Context context) {
        super(itemView);
        initView(itemView, context);
    }

    public void initView(View itemView, Context context) {
        this.context = context;

        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT
                , RecyclerView.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this, itemView);
        size60 = DeviceUtils.dipToPX(context,60);
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(POFocus data, int dataSize) {
        if(data==null){
            return;
        }

        FrescoUtils.bind(hotItemImg, data.getCover());
        hotItemNickname.setText(data.getMaster().getNickname());
        hotItemCity.setText(data.getCity());
        FrescoUtils.bind(hotItemHeader,data.getMaster().getAvatar(),size60,size60);
        hotItemTitle.setText(data.getTitle());
        hotItemWatch.setText(data.getOnline_users());
    }

}
