package one.show.live.po;

import java.io.Serializable;

/**
 * Created by liuzehua on 2018/4/4.
 */

public class POAddress implements Serializable {
    String name;//昵称
    String address;//地址
    String password;//资金密码
    boolean certification;//是否认证账户

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCertification() {
        return certification;
    }

    public void setCertification(boolean certification) {
        this.certification = certification;
    }
}
