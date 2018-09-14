package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.api.BaseRequest;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.AppUtil;
import one.show.live.widget.TitleView;

public class FlipTextViewFragment extends BaseFragmentActivity {


    private FlipViewController flipView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        flipView = new FlipViewController(inflater.getContext());

        flipView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                NumberTextView view;
                if (convertView == null) {
                    final Context context = parent.getContext();
                    view = new NumberTextView(context, position);
                    view.setTextSize(context.getResources().getDimension(R.dimen.textSize));
                } else {
                    view = (NumberTextView) convertView;
                    view.setNumber(position);
                }

                return view;
            }
        });

        return flipView;
    }

    @Override
    public void onResume() {
        super.onResume();
        flipView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        flipView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        flipView = null;
    }
}
}
