package one.show.live.live.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 18/3/31.
 */
public class POIMSystemMsg implements Parcelable {

    private int type;
    private String value;


    public POIMSystemMsg() {

    }

    protected POIMSystemMsg(Parcel in) {
        type = in.readInt();
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<POIMSystemMsg> CREATOR = new Creator<POIMSystemMsg>() {
        @Override
        public POIMSystemMsg createFromParcel(Parcel in) {
            return new POIMSystemMsg(in);
        }

        @Override
        public POIMSystemMsg[] newArray(int size) {
            return new POIMSystemMsg[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
