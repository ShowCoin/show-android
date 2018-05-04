package one.show.live.po;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nano on 2018/4/4.
 * <p>
 * 存储提现地址的po
 */

public class POAddressList implements Serializable {

    List<POAddress> poAddressList;

    public List<POAddress> getPoAddressList() {
        return poAddressList;
    }

    public void setPoAddressList(List<POAddress> poAddressList) {
        this.poAddressList = poAddressList;
    }



}
