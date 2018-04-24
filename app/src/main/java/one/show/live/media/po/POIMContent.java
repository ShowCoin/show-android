package one.show.live.media.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 16/5/31.
 */
public class POIMContent implements Parcelable {

//    {"content":""}

    /**
     * 消息内容
     */
    private String content;


    public POIMContent() {

    }


    protected POIMContent(Parcel in) {
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<POIMContent> CREATOR = new Creator<POIMContent>() {
        @Override
        public POIMContent createFromParcel(Parcel in) {
            return new POIMContent(in);
        }

        @Override
        public POIMContent[] newArray(int size) {
            return new POIMContent[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "POIMContent{" +
                "content='" + content + '\'' +
                '}';
    }
}
