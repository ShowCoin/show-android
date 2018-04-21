package one.show.live.media.gift.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import one.show.live.R;
import one.show.live.common.util.ConvertToUtils;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.util.FrescoUtils;
import one.show.live.media.po.POGift;
import one.show.live.po.GiftBean;

public class GiftAdapter extends BaseAdapter {

    private Context mContext;
    private List<POGift> list = new ArrayList<>();
    public int pagePosition;

    private int width;


    private Handler mHandler;

    public GiftAdapter(Activity context, int position, Handler handler) {
        this.mContext = context;
        this.pagePosition = position;
        this.mHandler = handler;
        width = DeviceUtils.getScreenWidth(context);
    }

    public void setData(List<POGift> list) {
        if (null != list) {
            this.list = list;
        }
    }

    @Override
    public int getCount() {
        int size = (list == null ? 0 : list.size() - SendGiftsView.GRIDVIEW_ITEM_COUNT
                * pagePosition);
        if (size > SendGiftsView.GRIDVIEW_ITEM_COUNT) {
            return SendGiftsView.GRIDVIEW_ITEM_COUNT;
        } else {
            return size > 0 ? size : 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int nowPosition = SendGiftsView.GRIDVIEW_ITEM_COUNT * pagePosition + position;
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gifts, null);
            holder.giftLay = (LinearLayout) convertView.findViewById(R.id.item_gift_lay);
            holder.giftImv = (SimpleDraweeView) convertView.findViewById(R.id.gift_imv);
            holder.giftTypeImv = (ImageView) convertView.findViewById(R.id.gift_type_sign);
            holder.giftCheckedImv = (ImageView) convertView.findViewById(R.id.gift_checked_imv);
            holder.giftName = (TextView) convertView.findViewById(R.id.gift_name_txt);
            holder.giftValue = (TextView) convertView.findViewById(R.id.gift_value_txt);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout
                    .LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.width = width / 5;
            params.height = ConvertToUtils.dipToPX(mContext,83);
            holder.giftLay.setLayoutParams(params);
            holder.giftCheckedImv.setLayoutParams(params);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final POGift bean = list.get(nowPosition);


        holder.giftName.setText(String.format("%s show",bean.getPrice()));
        holder.giftValue.setText(String.format("+%d 贡献值",bean.getExp()));

        if (bean.isCheck) {
            holder.giftCheckedImv.setVisibility(View.VISIBLE);
        } else {
            holder.giftCheckedImv.setVisibility(View.GONE);
        }

        if ("" != bean.getImage() && !bean.getImage().equals(holder.giftImv
                .getTag())) {
            holder.giftImv.setTag(bean.getImage());
            FrescoUtils.smallReqImage(holder.giftImv,bean.getImage());
        }
//        if (bean.getIsbursts() == 1 || bean.getAnimationtype() == 3) {//连击或是飘,显示右上角图标
//            holder.giftTypeImv.setVisibility(View.VISIBLE);
//            if (bean.getIsbursts() == 1) {
//                holder.giftTypeImv.setImageResource(R.drawable.gift_type_double_hit);
//            } else {
//                holder.giftTypeImv.setImageResource(R.drawable.gift_type_fly);
//            }
//        } else {
//            holder.giftTypeImv.setVisibility(View.GONE);
//        }


        if(bean.getType()==1){//type为1的时候就显示连发的图标
            holder.giftTypeImv.setVisibility(View.VISIBLE);
        }else{
            holder.giftTypeImv.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = mHandler.obtainMessage();
                message.what = nowPosition;
                message.obj = bean;
                message.sendToTarget();
            }
        });
        return convertView;
    }

    class ViewHolder {
        LinearLayout giftLay;
        SimpleDraweeView giftImv;
        ImageView giftCheckedImv, giftTypeImv;
        TextView giftName, giftValue;
    }
}
