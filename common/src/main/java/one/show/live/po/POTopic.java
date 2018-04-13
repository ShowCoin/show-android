package one.show.live.po;

import java.io.Serializable;

/**
 * Created by ydeng on 2017/6/9.
 */
//话题
public class POTopic implements Serializable {

    /**
     * id : 1
     * name : 健身
     * image : http://video-01.ws.seeulive.cn/591d5458ec9cdcc2fd2f8aa7.jpg
     * priority : 1   //优先级 0：最优
     * content : 大家一起来健身吧！
     */

    private int id;
    private String name;
    private String image;
    private int priority;
    private int viewers;//观看人数
    private String content;
    private int type;//1代表吃红包话题，2是正常话题

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getViewers() {
        return viewers;
    }

    public void setViewers(int viewers) {
        this.viewers = viewers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
