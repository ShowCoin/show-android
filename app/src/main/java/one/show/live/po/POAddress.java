package one.show.live.po;

import java.io.Serializable;

/**
 * Created by Nano on 2018/4/4.
 */

public class POAddress implements Serializable {

    /**
     * address : dhdhdh
     * name : dhdhdh
     * id : 8
     * type : 3
     *
     */

    private String address;
    private String name;
    private int id;
    
    private int type;

    private int isVerify;//判断是不是认证账户


    public int getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(int isVerify) {
        this.isVerify = isVerify;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
