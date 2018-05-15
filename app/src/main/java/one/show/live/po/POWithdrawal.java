package one.show.live.po;

import java.io.Serializable;

/**
 * Created by Nano on Show.
 */

public class POWithdrawal implements Serializable {

    /**
     * showNumber : 1100.000000000000000000
     * name : liuzehua
     * to_address : dhdhdhshhajajjj
     * comment :
     * status : 提现进行中
     */

    private String showNumber;
    private String name;
    private String to_address;
    private String comment;
    private String status;

    public String getShowNumber() {
        return showNumber;
    }

    public void setShowNumber(String showNumber) {
        this.showNumber = showNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
