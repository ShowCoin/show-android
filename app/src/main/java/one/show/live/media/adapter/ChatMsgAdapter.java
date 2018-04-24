package one.show.live.media.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import one.show.live.R;
import one.show.live.common.util.ConvertToUtils;
import one.show.live.common.util.StringUtils;
import one.show.live.common.view.recycler.BaseAdapter;
import one.show.live.media.po.POIMMsg;
import one.show.live.media.po.POIMTalkMsg;
import one.show.live.media.widget.DraweeSpan;
import one.show.live.media.widget.LinkTouchMovementMethod;
import one.show.live.media.widget.MyImageSpan;
import one.show.live.media.widget.TouchableSpan;


public class ChatMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<POIMTalkMsg> list;
    protected BaseAdapter.OnItemClickListener onItemClickListener;
    protected RecyclerView recyclerView;


    private int userNameColor;
    private int systemColor;
    private int roomColor;

    private int dp27;
    private int dp23;

    private int normalContentColor;

    private OnItemClickListener listener;

    private int[] contentColors = new int[5];

    public interface OnItemClickListener {
        void onClickNickName(String uid);

        void onLongClickNickName(String uid, String userName);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public ChatMsgAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();

        userNameColor = Color.parseColor("#B5A255");
        systemColor = Color.parseColor("#2BD2B6");
        roomColor = Color.parseColor("#FF6096");
        dp27 = ConvertToUtils.dipToPX(context, 27);
        dp23 = ConvertToUtils.dipToPX(context, 23);

        contentColors[0] = Color.parseColor("#FF4D6A");//消息类型1、7
        contentColors[1] = Color.parseColor("#A2FF00");//消息类型2
        normalContentColor = contentColors[2] = Color.parseColor("#FFFFFF");//普通用户发言消息，欢迎语(消息类型9）
        contentColors[3] = Color.parseColor("#FF6096");//消息类型10
        contentColors[4] = Color.parseColor("#BBA2FF");//消息类型11~14
    }


    /**
     * 根据消息类型选择字体颜色
     *
     * @param typeObj
     * @return
     */
    public int getColorByMsgType(POIMMsg.MsgType typeObj) {

        if (typeObj == null)
            return normalContentColor;

        switch (typeObj) {
            case GIFT:
            case LIKE:
                return contentColors[0];
            case LEVEL:
                return contentColors[1];
            case WELCOME:
                return contentColors[2];
            case CONTENT:
                return contentColors[3];
            case RANKLIST:
            case PERMISSION:
            case ADMIN:
            case ATTENDANCHOR:
                return contentColors[4];
        }

        return normalContentColor;
    }

    public void add(int location, POIMTalkMsg object) {
        list.add(location, object);
        if (list.size() > 10000) {
            list.remove(10000);
        }
    }

    public boolean add(POIMTalkMsg object) {
        return list.add(object);
    }

    public POIMTalkMsg get(int location) {
        return list.get(location);
    }

    public int size() {
        return list.size();
    }

    class MyMessageHolder extends RecyclerView.ViewHolder {
        private TextView msgTV;

        public MyMessageHolder(View itemView) {
            super(itemView);
            msgTV = (TextView) itemView.findViewById(R.id.content_tv);
            msgTV.setMovementMethod(new LinkTouchMovementMethod());
        }
    }

    public void setOnItemClickListener(RecyclerView recyclerView, BaseAdapter.OnItemClickListener onItemClickListener) {
        this.recyclerView = recyclerView;
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyMessageHolder(View.inflate(parent.getContext(), R.layout.item_msg_system, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        POIMTalkMsg bean = list.get(position);
        ((MyMessageHolder) holder).msgTV.setTag(bean);
        ((MyMessageHolder) holder).msgTV.setText(buildMsg(bean));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private static String imgFlag = "[img]";
    //    private static String systemTitle = " 系统消息: ";
//    private static String roomTitle = " 房间消息: ";
//    private static String offcialTitle = " 官方消息: ";
    private static String giftFlag = "[gift]";
    private static String likeFlag = "[like]";
    private static String blankFlag = " ";


    private SpannableStringBuilder buildMsg(POIMTalkMsg bean) {
        switch (bean.getType()) {
            case SystemType:
                return buildSystemMsg(bean.getContent());
            case LiveRoomAndWithUser:
                return buildRoomWithUserClick(bean);
            case LiveRoomType:
                return buildRoomMsg(bean.getContent());
            case NormalType:
                return buildNorMal(bean);
            case SendGift:
                return buildGift(bean);
            case LikeType:
                return buildLike(bean);

        }
        return null;
    }

    /**
     * 送礼物消息，尾部加上礼物icon
     *
     * @param bean
     * @return
     */
    private SpannableStringBuilder buildGift(final POIMTalkMsg bean) {

        String subTitle = getNameTitle(bean.getNickName());
        SpannableStringBuilder spanText = new SpannableStringBuilder(imgFlag);
        spanText.append(subTitle);
        spanText.append(bean.getContent());

        int giftLength = 0;
        if (StringUtils.isNotEmpty(bean.getGiftImgUrl())) {
            spanText.append(giftFlag);
            giftLength = giftFlag.length();
        }

        spanText.setSpan(mClickSpan, 0, imgFlag.length() + subTitle.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new MyImageSpan(bean.getIsAdmin() ? getAdminDrawable() : getLevelDrawable(bean.getLevel())), 0, imgFlag.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(userNameColor), imgFlag.length(), imgFlag.length() + subTitle.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(getColorByMsgType(bean.getMsgType())), imgFlag.length() + subTitle.length(), spanText.length() - giftLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        if (giftLength != 0) {
            spanText.setSpan(new DraweeSpan(bean.getGiftImgUrl()).setSize(dp23, dp23), spanText.length() - giftLength, spanText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        return spanText;

    }


    /**
     * 点赞消息，尾部加上赞icon
     *
     * @param bean
     * @return
     */
    private SpannableStringBuilder buildLike(final POIMTalkMsg bean) {
        String subTitle = getNameTitle(bean.getNickName());
        SpannableStringBuilder spanText = new SpannableStringBuilder(imgFlag);
        spanText.append(subTitle);
        spanText.append(bean.getContent());
        spanText.append(likeFlag);
        spanText.setSpan(mClickSpan, 0, imgFlag.length() + subTitle.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new MyImageSpan(bean.getIsAdmin() ? getAdminDrawable() : getLevelDrawable(bean.getLevel())), 0, imgFlag.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(userNameColor), imgFlag.length(), imgFlag.length() + subTitle.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(getColorByMsgType(bean.getMsgType())), imgFlag.length() + subTitle.length(), spanText.length() - likeFlag.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new MyImageSpan(getLikeDrawable()), spanText.length() - likeFlag.length(), spanText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanText;

    }

    /**
     * 房间消息，但是消息体内包含用户昵称
     *
     * @param bean
     * @return
     */
    private SpannableStringBuilder buildRoomWithUserClick(final POIMTalkMsg bean) {
        SpannableStringBuilder spanText = new SpannableStringBuilder(imgFlag);
        spanText.append(blankFlag);
        spanText.append(bean.getContent());


        spanText.setSpan(new MyImageSpan(getRoomDrawable()), 0, imgFlag.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        int clickStartIndex = imgFlag.length() + blankFlag.length() + bean.getNameStartIndex();

        ForegroundColorSpan contentColorSpan = new ForegroundColorSpan(getColorByMsgType(bean.getMsgType()));

        spanText.setSpan(mClickSpan, clickStartIndex, clickStartIndex + bean.getNickName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(userNameColor), clickStartIndex, clickStartIndex + bean.getNickName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);


        if (bean.getNameStartIndex() != 0) {
            spanText.setSpan(contentColorSpan, imgFlag.length() + blankFlag.length(), clickStartIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if (StringUtils.isNotEmpty(bean.getOptUid())) {
            int clickOptIndex = clickStartIndex + bean.getNickName().length() + bean.getOptStartIndexOffset();

            spanText.setSpan(mClickOptSpan, clickOptIndex, clickOptIndex + bean.getOptUserName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            if (bean.getOptStartIndexOffset() != 0) {
                spanText.setSpan(contentColorSpan, clickStartIndex + bean.getNickName().length(), clickOptIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            spanText.setSpan(new ForegroundColorSpan(userNameColor), clickOptIndex, clickOptIndex + bean.getOptUserName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanText.setSpan(contentColorSpan, clickOptIndex + bean.getOptUserName().length(), spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        } else {
            spanText.setSpan(contentColorSpan, clickStartIndex + bean.getNickName().length(), spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        return spanText;

    }

    /**
     * 普通直播间消息，用户之间发送的消息，不经过server
     *
     * @param bean
     * @return
     */
    private SpannableStringBuilder buildNorMal(final POIMTalkMsg bean) {

        String subTitle = getNameTitle(bean.getNickName());

        SpannableStringBuilder spanText = new SpannableStringBuilder(imgFlag);
        spanText.append(subTitle);
        spanText.append(bean.getContent());
        spanText.setSpan(mClickSpan, 0, imgFlag.length() + subTitle.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        spanText.setSpan(new MyImageSpan(bean.getIsAdmin() ? getAdminDrawable() : getLevelDrawable(bean.getLevel())), 0, imgFlag.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);


        spanText.setSpan(new ForegroundColorSpan(userNameColor), imgFlag.length(), imgFlag.length() + subTitle.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(normalContentColor), imgFlag.length() + subTitle.length(), spanText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return spanText;

    }


    private SpannableStringBuilder buildRoomMsg(String content) {
        if (StringUtils.isNotEmpty(content)) {
            SpannableStringBuilder spanText = new SpannableStringBuilder(imgFlag);
            spanText.append(blankFlag);
            spanText.append(content);
            spanText.setSpan(new MyImageSpan(getRoomDrawable()), 0, imgFlag.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanText.setSpan(new ForegroundColorSpan(roomColor), imgFlag.length() + blankFlag.length(), spanText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return spanText;
        }
        return null;
    }

    private SpannableStringBuilder buildSystemMsg(String content) {
        if (StringUtils.isNotEmpty(content)) {
            SpannableStringBuilder spanText = new SpannableStringBuilder(imgFlag);

            spanText.append(blankFlag);
            spanText.append(content);
            spanText.setSpan(new MyImageSpan(getSystemDrawable()), 0, imgFlag.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanText.setSpan(new ForegroundColorSpan(systemColor), imgFlag.length() + blankFlag.length(), spanText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return spanText;
        }
        return null;
    }


    TouchableSpan mClickSpan = new TouchableSpan() {
        @Override
        public boolean onLongClick(View widget) {
            if (listener != null && widget != null && widget.getTag() != null) {
                POIMTalkMsg bean = (POIMTalkMsg) widget.getTag();
                if (StringUtils.isNotEmpty(bean.getUid())) {
                    listener.onLongClickNickName(bean.getUid(), bean.getNickName());
                }
            }
            resetTouchableState();
            return false;
        }

        @Override
        public boolean onClick(View widget) {
            if (listener != null && widget != null && widget.getTag() != null) {
                POIMTalkMsg bean = (POIMTalkMsg) widget.getTag();
                if (StringUtils.isNotEmpty(bean.getUid())) {
                    listener.onClickNickName(bean.getUid());
                }
            }
            resetTouchableState();
            return false;
        }


    };


    private void resetTouchableState() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LinkTouchMovementMethod.touched = false;
            }
        }, 500);
    }

    TouchableSpan mClickOptSpan = new TouchableSpan() {
        @Override
        public boolean onLongClick(View widget) {
            if (listener != null && widget != null && widget.getTag() != null) {
                POIMTalkMsg bean = (POIMTalkMsg) widget.getTag();
                if (StringUtils.isNotEmpty(bean.getOptUid())) {
                    listener.onLongClickNickName(bean.getOptUid(), bean.getOptUserName());
                }
            }
            resetTouchableState();
            return false;
        }

        @Override
        public boolean onClick(View widget) {
            if (listener != null && widget != null && widget.getTag() != null) {
                POIMTalkMsg bean = (POIMTalkMsg) widget.getTag();
                if (StringUtils.isNotEmpty(bean.getOptUid())) {
                    listener.onClickNickName(bean.getOptUid());
                }
            }
            resetTouchableState();
            return false;
        }

    };


    private String getNameTitle(String name) {
        return String.format(" %s: ", name);
    }

    private Drawable getLevelDrawable(int level) {
        Drawable d = ContextCompat.getDrawable(context, context.getResources().getIdentifier("level_" + (level <= 0 ? 1 : level), "drawable", "tv.beke"));
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        return d;
    }


    private Drawable mSystemDrawable;

    private Drawable getSystemDrawable() {
        if (mSystemDrawable == null) {
            mSystemDrawable = ContextCompat.getDrawable(context, R.drawable.level_sym);
            mSystemDrawable.setBounds(0, 0, mSystemDrawable.getIntrinsicWidth(), mSystemDrawable.getIntrinsicHeight());
        }
        return mSystemDrawable;
    }

    private Drawable mRoomDrawable;

    private Drawable getRoomDrawable() {
        if (mRoomDrawable == null) {
            mRoomDrawable = ContextCompat.getDrawable(context, R.drawable.level_room);
            mRoomDrawable.setBounds(0, 0, mRoomDrawable.getIntrinsicWidth(), mRoomDrawable.getIntrinsicHeight());
        }
        return mRoomDrawable;
    }


    private Drawable mAdminDrawable;

    private Drawable getAdminDrawable() {
        if (mAdminDrawable == null) {
            mAdminDrawable = ContextCompat.getDrawable(context, R.drawable.xunguan);
            mAdminDrawable.setBounds(0, 0, mAdminDrawable.getIntrinsicWidth(), mAdminDrawable.getIntrinsicHeight());
        }
        return mAdminDrawable;
    }

    private Drawable mLikeDrawable;

    private Drawable getLikeDrawable() {
        if (mLikeDrawable == null) {
            mLikeDrawable = ContextCompat.getDrawable(context, R.drawable.hongtao);
            mLikeDrawable.setBounds(0, 0, dp27, dp23);
        }
        return mLikeDrawable;
    }


}
