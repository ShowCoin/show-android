package one.show.live.po;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Nano on Show.
 * <p>
 * 交易记录
 */

public class POTransactionRecords implements Serializable {


    /**
     * date : 2018-05-25 18:29:11
     * transaction_id : 999960564249788416
     * to_nickname : 雷猴啊
     * service_fee : 0
     * to_address : 0xd1758a88fd004d8894d042af737129d6229864fb
     * from_avatar : sysupload/5b0417421320b0e4348af495.png
     * from_uid : 100
     * showNumber : 1008611
     * to_uid : 987682897945296896
     * from_nickname : 系统
     * to_avatar : http://show-live.oss-cn-beijing.aliyuncs.com/avatar/5af6ba9f3c9ab0e4ad2a9c83.png
     * comment :
     * from_address : 0x1ae7c4157aa8364c6f065c9881934d9e9a5adc28
     */

    private String date;
    private String transaction_id;
    private String to_nickname;
    private String service_fee;
    private String to_address;
    private String from_avatar;
    private String from_uid;
    private BigDecimal showNumber;
    private String to_uid;
    private String from_nickname;
    private String to_avatar;
    private String comment;
    private String from_address;

    //下面这两个字段是提现记录用到的
    private int coinType;
    private int status;

    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTo_nickname() {
        return to_nickname;
    }

    public void setTo_nickname(String to_nickname) {
        this.to_nickname = to_nickname;
    }

    public String getService_fee() {
        return service_fee;
    }

    public void setService_fee(String service_fee) {
        this.service_fee = service_fee;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getFrom_avatar() {
        return from_avatar;
    }

    public void setFrom_avatar(String from_avatar) {
        this.from_avatar = from_avatar;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public BigDecimal getShowNumber() {
        return showNumber;
    }

    public void setShowNumber(BigDecimal showNumber) {
        this.showNumber = showNumber;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getFrom_nickname() {
        return from_nickname;
    }

    public void setFrom_nickname(String from_nickname) {
        this.from_nickname = from_nickname;
    }

    public String getTo_avatar() {
        return to_avatar;
    }

    public void setTo_avatar(String to_avatar) {
        this.to_avatar = to_avatar;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }
}
