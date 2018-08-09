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

public class DsznzActivity extends Activity {
    private ArrayList<HashMap<String, String>> listItem;
    private ListView list_ylfn;

    Button btnPre, btnNext;
    View.OnClickListener clickListener;

    // 用于显示每列5个Item项。
    int VIEW_COUNT = 10;

    // 用于显示页号的索引
    int index = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_ylfn);

        list_ylfn = (ListView) findViewById(R.id.listYlfn);
        btnPre = (Button) findViewById(R.id.btnPre);
        btnNext = (Button) findViewById(R.id.btnNext);

        listItem = new ArrayList<HashMap<String, String>>();

        HttpClient client = new DefaultHttpClient();
        HttpEntity entity = null;
        try {
            String uri = GetConnParams.getConnUri()
                    + "/phone_listYlfn?zgy.zgynum=" + zgynumLoged;
            //此处是从服务端获取数据，有些代码就省略了
            HttpPost request = new HttpPost(uri);
            HttpResponse response;
            response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                entity = response.getEntity();
            }
            String json = EntityUtils.toString(entity, "UTF-8").trim();

            JSONArray array = new JSONArray(URLDecoder.decode(json, "utf-8"));
            for (int i = 0; i < array.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ylfn_did", array.getJSONObject(i).getString("did"));
                map.put("ylfn_name", array.getJSONObject(i).getString("name"));
                map.put("gmsfz", array.getJSONObject(i).getString("gmsfz"));
                listItem.add(map);
                tmpListItem.add(map);
            }
//          // 生成适配器的Item和动态数组对应的元素
//          SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
//                  R.layout.ylfn,// ListItem的XML实现
//                  // 动态数组与ImageItem对应的子项
//                  new String[] { "ylfn_did", "ylfn_name", "gmsfz" },
//                  // ImageItem的XML文件里面的一个ImageView,两个TextView ID
//                  new int[] { R.id.ylfn_did, R.id.ylfn_name, R.id.gmsfz });
//
            myAdapter = new MyAdapter(this);
            list_ylfn.setAdapter(myAdapter);

            clickListener = new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    switch (v.getId()) {
                        case R.id.btnPre:
                            preView();
                            break;
                        case R.id.btnNext:
                            nextView();
                            break;
                    }
                }

            };

            // 添加2个Button的监听事件。
            btnPre.setOnClickListener(clickListener);
            btnNext.setOnClickListener(clickListener);
            // 检查2个Button是否是可用的
            checkButton();

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (entity != null)
                    entity.consumeContent();
                client.getConnectionManager().shutdown();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    // 点击左边的Button，表示向前翻页，索引值要减1.
    public void preView() {
        index--;

        // 刷新ListView里面的数值。
        myAdapter.notifyDataSetChanged();

        // 检查Button是否可用。
        checkButton();
    }

    // 点击右边的Button，表示向后翻页，索引值要加1.
    public void nextView() {
        index++;

        // 刷新ListView里面的数值。
        myAdapter.notifyDataSetChanged();

        // 检查Button是否可用。
        checkButton();
    }

    public void checkButton() {
        // 索引值小于等于0，表示不能向前翻页了，以经到了第一页了。
        // 将向前翻页的按钮设为不可用。
        if (index <= 0) {
            btnPre.setEnabled(false);
        } else {
            btnPre.setEnabled(true);
        }
        // 值的长度减去前几页的长度，剩下的就是这一页的长度，如果这一页的长度比View_Count小，表示这是最后的一页了，后面在没有了。
        // 将向后翻页的按钮设为不可用。
        if (listItem.size() - index * VIEW_COUNT <= VIEW_COUNT) {
            btnNext.setEnabled(false);
        }
        // 否则将2个按钮都设为可用的。
        else {
            btnNext.setEnabled(true);
        }

    }

    // ListView的Adapter，这个是关键的导致可以分页的根本原因。
    public class MyAdapter extends BaseAdapter {
        Activity activity;

        public MyAdapter(Activity a) {
            activity = a;
        }

        // 设置每一页的长度，默认的是View_Count的值。
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            // return data.length;

            // ori表示到目前为止的前几页的总共的个数。
            int ori = VIEW_COUNT * index;

            // 值的总个数-前几页的个数就是这一页要显示的个数，如果比默认的值小，说明这是最后一页，只需显示这么多就可以了
            if (listItem.size() - ori < VIEW_COUNT) {
                return listItem.size() - ori;
            }
            // 如果比默认的值还要大，说明一页显示不完，还要用换一页显示，这一页用默认的值显示满就可以了。
            else {
                return VIEW_COUNT;
            }

        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        //重点是getView方法
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            // return addTestView(position);
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.ylfn, null);
            TextView ylfn_did_view = (TextView) convertView.findViewById(R.id.ylfn_did);
            TextView ylfn_name_view = (TextView) convertView.findViewById(R.id.ylfn_name);
            TextView ylfn_gmsfz_view = (TextView) convertView.findViewById(R.id.gmsfz);
            ylfn_did_view.setText(listItem.get(position + index * VIEW_COUNT).get("ylfn_did"));
            ylfn_name_view.setText(listItem.get(position + index * VIEW_COUNT).get("ylfn_name"));
            ylfn_gmsfz_view.setText(listItem.get(position + index * VIEW_COUNT).get("gmsfz"));
            return convertView;
        }
    }
}  }
