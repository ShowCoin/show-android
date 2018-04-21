package one.show.live.po;

import java.io.Serializable;

/**
 * Created by liuzehua on 2018/4/21.
 */

public class POThird implements Serializable {

    String name;
    String headimage;
    int  sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
