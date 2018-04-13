package one.show.live.common.po;

import java.util.List;

public class POListData<T> {
    private List<T> list;   //数组列表
    /**
     * 下一页数据的起始下标
     */
    private int next_cursor;
    /**
     * 当前页面条数
     */
    private int count;
    /**
     * 首页用到的未读消息数量
     */
    private int unreadCount;
    /**
     * 首页用到的未读的
     */
    private int new_follow_work_count;

    /**
     * 个人中心页面列表数据用到的判断是不是好友，1是0不是
     * @return
     */
    private int isFriend;
    /**
     * 个人中心页面列表数据用到的判断是不是被拉黑，1是0不是
     */
    private int isBlack;
    /**
     * 最近访客的总访客人数
     */
    private int visitCount;
    /**
     * 最近访客的24小时内的访客人数
     */
    private int visitCountDaily;

    private String content;//话题详情的 介绍
    private int id;//话题详情的 id
    private int viewers;//话题详情的 观看人数
    private int priority;//话题详情的 优先级
    private String name;//话题详情的 话题名称
    private String image;//话题详情的 背景大图

    private POTopic topic;

    public POTopic getTopic() {
        return topic;
    }

    public void setTopic(POTopic topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getViewers() {
        return viewers;
    }

    public void setViewers(int viewers) {
        this.viewers = viewers;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getVisitCountDaily() {
        return visitCountDaily;
    }

    public void setVisitCountDaily(int visitCountDaily) {
        this.visitCountDaily = visitCountDaily;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    /**
     * 新鲜页面用到的
     */
    private String lastRefreshTime;//最后一次请求时间

    public String getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(String lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }

    public boolean IsBlack() {
        return isBlack==1;
    }

    public void setIsBlack(int isBlack) {
        this.isBlack = isBlack;
    }

    public boolean IsFriend() {
        return isFriend==1;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String text;//描述文本

    private List<PORecommends> recommends;


    private int offset;
    /**
     * conditions : {"tags":"主播,大叔,摄影师,歌手,主持人,暖男,演员,自由职业,设计师,模特"}
     * list : []
     */

    private ConditionsBean conditions;

    public int getNew_follow_work_count() {
        return new_follow_work_count;
    }

    public List<PORecommends> getRecommends() {
        return recommends;
    }

    public void setRecommends(List<PORecommends> recommends) {
        this.recommends = recommends;
    }

    public void setNew_follow_work_count(int new_follow_work_count) {
        this.new_follow_work_count = new_follow_work_count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }


    public int getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(int next_cursor) {
        this.next_cursor = next_cursor;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getUnreadCount() {

        return unreadCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * 是否还有下一页数据
     * @return
     */
    public boolean hasMore() {
        return next_cursor != -1;
    }

    public ConditionsBean getConditions() {
        return conditions;
    }

    public void setConditions(ConditionsBean conditions) {
        this.conditions = conditions;
    }


    public static class ConditionsBean {
        /**
         * tags : 主播,大叔,摄影师,歌手,主持人,暖男,演员,自由职业,设计师,模特
         */

        private String tags;

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }
        int minAge;
        int maxAge;

        public int getMinAge() {
            return minAge;
        }

        public void setMinAge(int minAge) {
            this.minAge = minAge;
        }

        public int getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(int maxAge) {
            this.maxAge = maxAge;
        }
    }
}
