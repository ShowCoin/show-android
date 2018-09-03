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



public class SwipeDismissActivity extends BaseFragmentActivity {


    @BindView(R.id.about_title)
    TitleView aboutTitle;
    @BindView(R.id.about_version)
    TextView aboutVersion;
    @BindView(R.id.about_community_convention)
    RelativeLayout aboutCommunityConvention;
    @BindView(R.id.about_privacy_policy)
    RelativeLayout aboutPrivacyPolicy;
    @BindView(R.id.about_terms_of_service)
    RelativeLayout aboutTermsOfService;
    @BindView(R.id.about_management_specification)
    RelativeLayout aboutManagementSpecification;
    @BindView(R.id.about_management_rules)
    RelativeLayout aboutManagementRules;
    @BindView(R.id.about_contact_us)
    RelativeLayout aboutContactUs;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, SwipeDismissActivity.class);
        return intent;
    }

    private void showDialog(String msg){
        AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                .setTitle("alert")
                .setMessage(msg)
                .setCancelable(false)
                .create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final RecyclerView anotherRecyclerView = (RecyclerView) findViewById(R.id.recyclerHorizontalView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this);
        horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(layoutManager);
        anotherRecyclerView.setLayoutManager(horizontalLayoutManager);

        List<String> dataset = new LinkedList<String>();
        for (int i = 0; i < 100; i++){
            dataset.add("item" + i);
        }
        final MyAdapter adapter = new MyAdapter(dataset);

        recyclerView.setAdapter(adapter);
        anotherRecyclerView.setAdapter(adapter);

        // Bind touch listener

        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(
                recyclerView,
                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view) {
                        int id = recyclerView.getChildPosition(view);
                        adapter.mDataset.remove(id);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(getBaseContext(), String.format("Delete item %d",id),Toast.LENGTH_LONG).show();
                    }
                })
                .setIsVertical(false)
                .setItemTouchCallback(
                        new SwipeDismissRecyclerViewTouchListener.OnItemTouchCallBack() {
                            @Override
                            public void onTouch(int index) {
                                showDialog(String.format("Click item %d", index));
                            }
                        })
                .create();

        recyclerView.setOnTouchListener(listener);


        // set touch listener for recyclerHorizontalView(horizontal swipe to remove)

        SwipeDismissRecyclerViewTouchListener verticalListener = new SwipeDismissRecyclerViewTouchListener.Builder(
                anotherRecyclerView,
                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view) {
                        int id = recyclerView.getChildPosition(view);
                        adapter.mDataset.remove(id);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(getBaseContext(), String.format("Delete item %d",id),Toast.LENGTH_LONG).show();
                    }
                }).setIsVertical(true).create();

        anotherRecyclerView.setOnTouchListener(verticalListener);


    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        public List<String> mDataset;

        public MyAdapter(List<String> dataset) {
            super();
            mDataset = dataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = View.inflate(viewGroup.getContext(), android.R.layout.simple_list_item_1, null);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.mTextView.setText(mDataset.get(i));
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView mTextView;
            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView;
            }
        }
    }
}
