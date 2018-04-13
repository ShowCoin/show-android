package one.show.live.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.R;
import one.show.live.po.POHot;
import one.show.live.util.FrescoUtils;
import one.show.live.view.recycler.SimpleHolder;

/**
 * Created by liuzehua on 2017/11/15.
 */

public class HotItem extends SimpleHolder {

    Context context;
    @BindView(R.id.hot_item_img)
    SimpleDraweeView hotItemImg;


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

    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(POHot data, int dataSize) {

        FrescoUtils.bind(hotItemImg, data.getNickName());

    }

}
