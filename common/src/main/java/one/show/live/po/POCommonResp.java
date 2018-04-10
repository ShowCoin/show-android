package one.show.live.po;

public class POCommonResp<T> {
    private int state;
    private String msg;
    private T data;
    private Ext ext;

    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return this.state == 1;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMsg(String msg) {

        this.msg = msg;
    }

    public void setState(int state) {

        this.state = state;
    }

    public T getData() {

        return data;
    }

    public String getMsg() {

        return msg;
    }

    public int getState() {

        return state;
    }

    public Ext getExt() {
        return ext;
    }

    public void setExt(Ext ext) {
        this.ext = ext;
    }

    public class Ext {
        private Message message;

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
    }

    public class Message {
        private int total;
        private int gift_message;
        private int at_count;
        private int comment_count;
        private int fans_count;
        private int praise_count;
        private int sys_count;
        private int treatise_count;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getAt_count() {
            return at_count;
        }

        public void setAt_count(int at_count) {
            this.at_count = at_count;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public int getFans_count() {
            return fans_count;
        }

        public void setFans_count(int fans_count) {
            this.fans_count = fans_count;
        }

        public int getPraise_count() {
            return praise_count;
        }

        public void setPraise_count(int praise_count) {
            this.praise_count = praise_count;
        }

        public int getSys_count() {
            return sys_count;
        }

        public void setSys_count(int sys_count) {
            this.sys_count = sys_count;
        }

        public int getTreatise_count() {
            return treatise_count;
        }

        public void setTreatise_count(int treatise_count) {
            this.treatise_count = treatise_count;
        }

        public int getGift_message() {
            return gift_message;
        }

        public void setGift_message(int gift_message) {
            this.gift_message = gift_message;
        }
    }
}
