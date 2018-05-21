package one.show.live.po;


import java.io.Serializable;

/**
 * Created by Nano on 2018/4/9.
 */

public class POFocus implements Serializable{


    private POMaster master;
    /**
     * room_id : 987338889393995776
     * receive : 1070
     * city : null
     * is_vip : 0
     * room_admin : 0
     * stream_addr : http://pull.souyu.tv/show/987338889393995776_1524234909.flv
     * stream_status : 2
     * title : ShowCoin新人16437069的直播
     * liked : 0
     * online_users : 13
     * master : {"master_level":1,"uid":984772429270351872,"gender":0,"nickname":"ShowCoin新人16437069","fan_level":1,"birth":0,"popularNo":23320785,"avatar":"http://show-live.oss-cn-beijing.aliyuncs.com/avatar/5ad5ac7d3b190ec2895f9548.jpeg","descriptions":null,"isFollowed":0}
     * vid : 987338889393995776
     * cover : http://show-live.oss-cn-beijing.aliyuncs.com/avatar/5ad5ac7d3b190ec2895f9548.jpeg
     * share_addr : http://www.xiubi.com/share/live/987338889393995776
     * is_live : 1
     * topic :
     * share : 0
     * id : 987338889393995776
     */

    private long room_id;
    private int receive;
    private String city;
    private int is_vip;
    private int room_admin;
    private String stream_addr;
    private int stream_status;
    private String title;
    private int liked;
    private int online_users;
    private long vid;
    private String cover;
    private String share_addr;
    private int is_live;
    private String topic;
    private int share;
    private String id;

    public POMaster getMaster() {
        return master;
    }

    public void setMaster(POMaster master) {
        this.master = master;
    }

    public long getRoom_id() {
        return room_id;
    }

    public void setRoom_id(long room_id) {
        this.room_id = room_id;
    }

    public int getReceive() {
        return receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public int getRoom_admin() {
        return room_admin;
    }

    public void setRoom_admin(int room_admin) {
        this.room_admin = room_admin;
    }

    public String getStream_addr() {
        return stream_addr;
    }

    public void setStream_addr(String stream_addr) {
        this.stream_addr = stream_addr;
    }

    public int getStream_status() {
        return stream_status;
    }

    public void setStream_status(int stream_status) {
        this.stream_status = stream_status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public int getOnline_users() {
        return online_users;
    }

    public void setOnline_users(int online_users) {
        this.online_users = online_users;
    }

    public long getVid() {
        return vid;
    }

    public void setVid(long vid) {
        this.vid = vid;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getShare_addr() {
        return share_addr;
    }

    public void setShare_addr(String share_addr) {
        this.share_addr = share_addr;
    }

    public int getIs_live() {
        return is_live;
    }

    public void setIs_live(int is_live) {
        this.is_live = is_live;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
