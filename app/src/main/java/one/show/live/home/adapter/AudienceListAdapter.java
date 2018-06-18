package one.show.live.home.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.R;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.util.FrescoUtils;
import one.show.live.media.po.POIMUser;
import one.show.live.util.StringUtil;
import one.show.live.widget.HeadImageView;

public class AudienceListAdapter extends BaseAdapter {
    private Context mContext;
    private int clickTemp = 0;
    List<POIMUser> data;


    public AudienceListAdapter(Context mContext, List<POIMUser> data) {
        this.data = data;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    public void setData(List<POIMUser> list) {
        data.clear();
        data.addAll(list);
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int potion, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.audience_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        POIMUser mdata = data.get(potion);
        holder.meHeader.setImage(mdata.getAvatar(),mdata.getUid(), DeviceUtils.dipToPX(mContext,40));
        holder.tvNickname.setText(mdata.getNickname());
        holder.tvMoney.setText(mdata.getShowCoinNum() + "");
        if (mdata.getGender() == 1) {//设置性别  1男2女0未知
            Drawable nav_up = ContextCompat.getDrawable(mContext, R.drawable.male);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            holder.mini_gender.setCompoundDrawables(nav_up, null, null, null);
        } else if (mdata.getGender() == 2) {
            Drawable nav_up = ContextCompat.getDrawable(mContext, R.drawable.female);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            holder.mini_gender.setCompoundDrawables(nav_up, null, null, null);
        } else {
            holder.mini_gender.setCompoundDrawables(null, null, null, null);
        }
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.livechat_header)
        HeadImageView meHeader;
        @BindView(R.id.me_header)
        TextView mini_gender;
        @BindView(R.id.tv_nickname)
        TextView tvNickname;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.ll_home_item)
        RelativeLayout llHomeItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface rechargeDiamondListener {
        void onTicketClick(String ticket, int potion);
    }

    public void setRechargeDiamondListener(AudienceListAdapter.rechargeDiamondListener rechargeDiamondListener) {
        this.rechargeDiamondListener = rechargeDiamondListener;
    }

    private rechargeDiamondListener rechargeDiamondListener;

}
