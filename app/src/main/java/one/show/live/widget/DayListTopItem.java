package one.show.live.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import one.show.live.R;
import one.show.live.common.view.recycler.SimpleHolder;
import one.show.live.po.PODayList;

public class DayListTopItem extends SimpleHolder {

    Context context;
    int sizeW;
    int sizeH;

    public DayListTopItem(Context context, ViewGroup parent, int num) {
        this(context, LayoutInflater.from(context).inflate(R.layout.item_daylist_top, parent, false), num);
    }

    public DayListTopItem(Context context, View view, int num) {
        super(view);
        this.context = context;
        ButterKnife.bind(this, view);
    }


    public void setData(final PODayList data) {
        if (data == null) {
            return;
        }

    }

}
