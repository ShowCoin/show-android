package one.show.live.common.view.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public abstract class BaseAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {
    protected List<T> items =  Collections.synchronizedList(new ArrayList<T>());
    protected OnItemClickListener onItemClickListener,onItemLongClickListener;
    protected RecyclerView recyclerView;

    public BaseAdapter() {
    }

    public void add(T object) {
        items.add(object);
    }

    public void add(int index, T object) {
        items.add(index, object);
    }

    public void addAll(Collection<? extends T> collection) {
        if (collection != null) {
            items.addAll(collection);
        }
    }

    public void addAllAndNotify(Collection<? extends T> collection) {
        addAll(collection);
        notifyDataSetChanged();
    }

    public void addAll(T... items) {
        addAll(Arrays.asList(items));
    }

    public void clear() {
        items.clear();
    }

    public void remove(int position) {
        items.remove(position);
    }
    public void remove(T obj) {
        items.remove(obj);
    }

    public void set(int position, T m) {
        items.set(position, m);
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public List<T> getItems() {
        return items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public int size() {
        return items.size();
    }

    /**
     * 设置点击事件
     *
     * @param vh   被点击的   ViewHolder
     * @param view 点击的     View
     */
    protected void onItemClick(RecyclerView.ViewHolder vh, View view) {
        if (recyclerView != null && onItemClickListener != null) {
            onItemClickListener.onItemClick(view, recyclerView.getChildAdapterPosition(vh.itemView));
        }
    }

    protected void onItemLongClick(RecyclerView.ViewHolder vh, View view) {
        if (recyclerView != null && onItemLongClickListener != null) {
            onItemLongClickListener.onItemClick(view, recyclerView.getChildAdapterPosition(vh.itemView));
        }
    }

    public void setOnItemClickListener(RecyclerView recyclerView, OnItemClickListener onItemClickListener) {
        this.recyclerView = recyclerView;
        this.onItemClickListener = onItemClickListener;
    }

    public void setItemOnLongClickListener(RecyclerView recyclerView, OnItemClickListener onItemClickListener) {
        this.recyclerView = recyclerView;
        this.onItemLongClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
