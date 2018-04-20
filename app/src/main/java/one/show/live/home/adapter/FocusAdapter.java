package one.show.live.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import one.show.live.widget.FocusItem;
import one.show.live.common.view.recycler.BaseBizAdapter;
import one.show.live.common.view.recycler.SimpleHolder;

/**
 * Created by apple on 18/3/21.
 * <p/>
 * 关注
 */
public class FocusAdapter extends BaseBizAdapter<POFocus, SimpleHolder> {
    int num;
    public FocusAdapter(int num){
        this.num = num;
    }


    @Override
    public SimpleHolder onCreateItemViewHolder(ViewGroup parent) {

        return new FocusItem(parent.getContext(), parent,num);
    }

    @Override
    public void onBindItemViewHolder(SimpleHolder holder, int position) {

        ((FocusItem) holder).setData(getItem(position));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payLoads) {

        if (payLoads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (holder instanceof FocusItem) {
                ((FocusItem) holder).setData(getItem(position));
            }
        }
    }

}
