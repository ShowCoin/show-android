package one.show.live.message.adapter;

import android.view.ViewGroup;

import one.show.live.common.view.recycler.simple.SimpleAdapter;
import one.show.live.common.view.recycler.simple.SimpleHolder;
import one.show.live.message.binder.IConversationListBinder;
import one.show.live.widget.ConversationListExtraEntriesItem;
import one.show.live.widget.ConversationListItem;

public class ConversationListAdapter extends SimpleAdapter<IConversationListBinder> {

    public final static int ITEM_TYPE_NORMAL = 0;
    public final static int ITEM_TYPE_EXTRA_ENTRY = 1;


    public ConversationListAdapter(IConversationListBinder binder) {
        super(binder);
    }


    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_EXTRA_ENTRY:
                return new ConversationListExtraEntriesItem(mBinder, parent);
            default:
                return new ConversationListItem(mBinder, parent);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mBinder.getData()) {
            return ITEM_TYPE_EXTRA_ENTRY;
        }
        return mBinder.getData()
                .size() + ITEM_TYPE_EXTRA_ENTRY;
    }

    @Override
    public Object getItem(int type, int subPosition) {
        switch (type) {
            case ITEM_TYPE_EXTRA_ENTRY:
                return 0;
            default:
                return mBinder.getData()
                        .get(subPosition);
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (position < ITEM_TYPE_EXTRA_ENTRY) {
            return ITEM_TYPE_EXTRA_ENTRY;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    @Override
    public int getSubPosition(int type, int position) {
        return position - ITEM_TYPE_EXTRA_ENTRY;
    }
}
