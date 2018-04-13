package one.show.live.live.po;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import one.show.live.log.Logger;


@MessageTag(value = "systemMsg")
public class POIMSystemMsgType extends MessageContent {


    private String content;


    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("content", content);
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }


    protected POIMSystemMsgType(Parcel in) {
        content = ParcelUtils.readFromParcel(in);
    }


    public POIMSystemMsgType(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("content"))
                content = jsonObj.optString("content");

        } catch (JSONException e) {
            Logger.e("JSONException", e.getMessage());
        }

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<POIMSystemMsgType> CREATOR = new Creator<POIMSystemMsgType>() {
        @Override
        public POIMSystemMsgType createFromParcel(Parcel in) {
            return new POIMSystemMsgType(in);
        }

        @Override
        public POIMSystemMsgType[] newArray(int size) {
            return new POIMSystemMsgType[size];
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
        return "POIMSystemMsg{" +
                "content='" + content + '\'' +
                '}';
    }


    public POIMSystemMsg parseMsg() {
        try {
            return new Gson().fromJson(content, POIMSystemMsg.class);
        }catch (Exception e){
            Logger.e("samuel","系统消息parse错误,content:"+content);
            e.printStackTrace();
        }
        return null;
    }


}
