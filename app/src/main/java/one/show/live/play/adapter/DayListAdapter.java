package one.show.live.play.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import one.show.live.common.view.recycler.BaseBizAdapter;
import one.show.live.common.view.recycler.ItemType;
import one.show.live.common.view.recycler.SimpleHolder;
import one.show.live.po.PODayList;
import one.show.live.widget.DayListItem;
import one.show.live.widget.DayListTopItem;

/**
 * Created by apple on 18/3/21.
 * <p/>
 * 榜单
 */
public class DayListAdapter extends BaseBizAdapter<PODayList, SimpleHolder> {
    int num;

    int TOP = 1;
    int BOTTOM = 2;
    int type;

    public DayListAdapter(int num) {
        this.num = num;
    }


    @Override
    public SimpleHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

        if(viewType == ItemType.TYPE_OTHER.ordinal()){
            type = TOP;
            return new DayListTopItem(parent.getContext(), parent, num);
        }else{
            return new DayListItem(parent.getContext(), parent, num);
        }


    }

    @Override
    public void onBindItemViewHolder(SimpleHolder holder, int position) {

        if(holder instanceof DayListTopItem){
            ((DayListTopItem) holder).setData(getItem(position));
        }else{
            ((DayListItem) holder).setData(getItem(position));
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payLoads) {

        if (payLoads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (holder instanceof DayListTopItem) {
                ((DayListTopItem) holder).setData(getItem(position));
            }else{
                ((DayListItem) holder).setData(getItem(position));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {


        if (position == 0) {
            return ItemType.TYPE_OTHER.ordinal();
        } else {
            return super.getItemViewType(position);
        }
    }
}
