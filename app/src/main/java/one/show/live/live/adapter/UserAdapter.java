package one.show.live.live.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import one.show.live.R;
import one.show.live.po.POMember;
import one.show.live.util.ConvertToUtils;
import one.show.live.util.DelayPostprocessor;
import one.show.live.util.FrescoUtils;
import one.show.live.util.WeakHandler;
import one.show.live.live.po.POIMUser;
import one.show.live.po.UserBean;


/**
 * 视频直播，用户
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<POIMUser> users;
    private WeakHandler mHandler;
    Context context;
    private int size;
    private int levelSize;




    public UserAdapter(WeakHandler mHandler) {
        this.mHandler = mHandler;
        users = new ArrayList<>();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView headerIV;
        SimpleDraweeView header_iv_level;

        public UserViewHolder(final View itemView) {
            super(itemView);
            headerIV = (SimpleDraweeView) itemView.findViewById(R.id.header_iv);
            header_iv_level = (SimpleDraweeView) itemView.findViewById(R.id.header_iv_level);
        }
    }

    public void setData(List<POIMUser> list) {
        users.clear();
        users.addAll(list);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        size = ConvertToUtils.dipToPX(context,44);
        levelSize = ConvertToUtils.dipToPX(context,15);
        View v = View.inflate(parent.getContext(), R.layout.item_user, null);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        final POIMUser bean = getUserAt(position);
        if (bean == null) {
            return;
        }
        String url = bean.getProfileImg();
//        holder.headerIV.setImageURI(FrescoUtils.getUri(url));
        FrescoUtils.bind(holder.headerIV,FrescoUtils.getUri(url),size);
        holder.headerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain();
                message.what = -1;
                message.obj = bean.getUid();
                mHandler.sendMessage(message);
            }
        });

        int shiwei = (bean.getFanLevel() <= 0 ? 1 : bean.getFanLevel()) / 10;
        int gewei = (bean.getFanLevel() <= 0 ? 1 : bean.getFanLevel()) % 10;

        if (gewei == 0) {//如果取余结果为0，就另行处理，因为10级和9级的图标是一样的，但是取整结果不一样，只能单独处理
            shiwei = shiwei - 1;
            gewei = 6;
        }
        gewei = gewei > 5 ? 6 : 1;

        FrescoUtils.bind(holder.header_iv_level,FrescoUtils.getResUri(context.getResources().getIdentifier(String.format("lovel_%d_%d",shiwei,gewei), "drawable", "tv.beke")),levelSize);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

//    /**
//     * 用户
//     * @param bean 用户
//     * @param type 1添加,2删除
//     */
//    public synchronized void changeUserBean(POIMUser bean, long memberID, int type) {
//        synchronized (this) {
//            users.remove(memberID);
//            if (type == 1) {
//                users.put(memberID, bean);
//            }
//        }
//    }

    /**
     * 获取用户信息
     *
     * @param index 用户所在索引位置
     * @return 用户信息
     */
    public synchronized POIMUser getUserAt(int index) {
        synchronized (this) {
            if (index > users.size()) {
                return null;
            }
            return users.get(index);
        }
    }
}
