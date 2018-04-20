package one.show.live.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import one.show.live.po.POHot;
import one.show.live.common.view.recycler.BaseBizAdapter;
import one.show.live.common.view.recycler.SimpleHolder;

/**
 * Created by apple on 18/3/21.
 * <p/>
 * 热门
 */
public class HotAdapter extends BaseBizAdapter<POHot, SimpleHolder> {


    @Override
    public SimpleHolder onCreateItemViewHolder(ViewGroup parent) {

        return new HotItem(parent.getContext(), parent);
    }

    @Override
    public void onBindItemViewHolder(SimpleHolder holder, int position) {

        ((HotItem) holder).setData(getItem(position), position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payLoads) {

        if (payLoads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (holder instanceof HotItem) {
                ((HotItem) holder).setData(getItem(position), position);
            }
        }
    }
}
