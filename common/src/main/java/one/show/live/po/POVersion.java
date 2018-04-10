package one.show.live.po;


import android.os.Parcel;
import android.os.Parcelable;

public class POVersion implements Parcelable {
    /**
     * update_title :
     * update_upgrade : 抢先体验
     * new_kernel_version :
     * version_state :
     * update_cancel : 残忍拒绝
     * update_ignore : 放弃机会
     * update_msg :
     * client_download_url :
     */

    private String update_title;
    private String update_upgrade;
    private String new_kernel_version;
    private int version_state;
    private String update_cancel;
    private String update_ignore;
    private String update_msg;
    private String client_download_url;

    public String getUpdate_title() {
        return update_title;
    }

    public void setUpdate_title(String update_title) {
        this.update_title = update_title;
    }

    public String getUpdate_upgrade() {
        return update_upgrade;
    }

    public void setUpdate_upgrade(String update_upgrade) {
        this.update_upgrade = update_upgrade;
    }

    public String getNew_kernel_version() {
        return new_kernel_version;
    }

    public void setNew_kernel_version(String new_kernel_version) {
        this.new_kernel_version = new_kernel_version;
    }

    public int getVersion_state() {
        return version_state;
    }

    public void setVersion_state(int version_state) {
        this.version_state = version_state;
    }

    public String getUpdate_cancel() {
        return update_cancel;
    }

    public void setUpdate_cancel(String update_cancel) {
        this.update_cancel = update_cancel;
    }

    public String getUpdate_ignore() {
        return update_ignore;
    }

    public void setUpdate_ignore(String update_ignore) {
        this.update_ignore = update_ignore;
    }

    public String getUpdate_msg() {
        return update_msg;
    }

    public void setUpdate_msg(String update_msg) {
        this.update_msg = update_msg;
    }

    public String getClient_download_url() {
        return client_download_url;
    }

    public void setClient_download_url(String client_download_url) {
        this.client_download_url = client_download_url;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.update_title);
        dest.writeString(this.update_upgrade);
        dest.writeString(this.new_kernel_version);
        dest.writeInt(this.version_state);
        dest.writeString(this.update_cancel);
        dest.writeString(this.update_ignore);
        dest.writeString(this.update_msg);
        dest.writeString(this.client_download_url);
    }

    public POVersion() {
    }

    protected POVersion(Parcel in) {
        this.update_title = in.readString();
        this.update_upgrade = in.readString();
        this.new_kernel_version = in.readString();
        this.version_state = in.readInt();
        this.update_cancel = in.readString();
        this.update_ignore = in.readString();
        this.update_msg = in.readString();
        this.client_download_url = in.readString();
    }

    public static final Parcelable.Creator<POVersion> CREATOR = new Parcelable.Creator<POVersion>() {
        @Override
        public POVersion createFromParcel(Parcel source) {
            return new POVersion(source);
        }

        @Override
        public POVersion[] newArray(int size) {
            return new POVersion[size];
        }
    };


//
//    /**
//     * 是否强制更新
//     */
//    public boolean force;
//    /**
//     * 地址
//     */
//    public String url;
//    /**
//     * 版本
//     */
//    public int version;
//
//    /**
//     * 更新信息
//     */
//    public String updateInfo;
}
