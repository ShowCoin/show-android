package one.show.live.home.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import one.show.live.R;
import one.show.live.home.adapter.HotAdapter;
import one.show.live.play.ui.PlayActivity;
import one.show.live.po.POHot;
import one.show.live.common.ui.BaseFragment;
import one.show.live.common.view.recycler.BaseAdapter;

/**
 * Created by Nano on 2018/4/8.
 * 热门
 */

public class HotFragment extends BaseFragment {

    @BindView(R.id.hot_viewpager)
    RecyclerViewPager hotViewpager;
    Unbinder unbinder;
    HotAdapter hotAdapter;

    public static HotFragment newInstance() {
        HotFragment hotFragment = new HotFragment();
        return hotFragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_hot;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        hotViewpager.setLayoutManager(layout);

        hotAdapter = new HotAdapter();

        List<POHot> listhot = new ArrayList<>();
        POHot poHot1 = new POHot();
        poHot1.setNickName("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=15232631" +
                "32864&di=fd23c4f4373c5c4150fa8e1a949e1556&imgtype=0&src=http%3A%2F%2Fwww.cnhuadong.net%2Fuploadfiles%2Fimages%2F2017-1-18%2F201701180942jpg57270.jpg");
        POHot poHot2 = new POHot();
        poHot2.setNickName("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=152326552" +
                "4984&di=e4ad37bc2d2f7a941de7fe09542fb958&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F37d3d539b6003af3a8282d86332ac65c1138b642.jpg");
         POHot poHot3 = new POHot();
        poHot3.setNickName("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523265524" +
                "982&di=31fc269a32a2465e79752d3e06b6eb43&imgtype=0&src=http%3A%2F%2Fimg.mp.sohu.com%2Fupload%2F20170615%2F7cda85dec82b4731bc4cb9a891a45fa4_th.png");

        listhot.add(poHot1);
        listhot.add(poHot2);
        listhot.add(poHot3);
        listhot.add(poHot1);
        listhot.add(poHot2);
        listhot.add(poHot3);
        listhot.add(poHot1);
        listhot.add(poHot2);
        listhot.add(poHot3);

        hotAdapter.addAll(listhot);

        hotViewpager.setAdapter(hotAdapter);
        hotAdapter.setOnItemClickListener(hotViewpager, new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
startActivity(PlayActivity.getCallingIntent(getContext()));
            }
        });

    }
}
