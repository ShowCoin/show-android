package one.show.live.media.po;

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


@MessageTag(value = "chatRoomMsg")
public class POIMCustomeMsgType extends MessageContent {


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


    protected POIMCustomeMsgType(Parcel in) {
        content = ParcelUtils.readFromParcel(in);
    }


    public POIMCustomeMsgType(byte[] data) {
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

    public static final Creator<POIMCustomeMsgType> CREATOR = new Creator<POIMCustomeMsgType>() {
        @Override
        public POIMCustomeMsgType createFromParcel(Parcel in) {
            return new POIMCustomeMsgType(in);
        }

        @Override
        public POIMCustomeMsgType[] newArray(int size) {
            return new POIMCustomeMsgType[size];
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
        return "POIMMsg{" +
                "content='" + content + '\'' +
                '}';
    }

//    public <T> String buildMsg(POIMMsg<T> msg) {
//        try {
//            Type type = new TypeToken<POIMMsg<T>>() {
//            }.getType();
//            return new Gson().toJson(msg, type);
//        }catch (Exception e){
//            e.printStackTrace();
////            return "{\"type\":"+MsgType.UNKNOW.type+",\"data\":{}}";
//            return null;
//        }
//    }


    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }


    public POIMMsg parseMsg() {
        try {

            JSONObject jsonObj = new JSONObject(content);

            int msgType = POIMMsg.MsgType.UNKNOW.type;
            if (jsonObj.has("type"))
                msgType = jsonObj.optInt("type");


            Class raw = null;
            Class clazz = null;

            if (msgType == POIMMsg.MsgType.STARTLIVE.type) {
                raw = POIMMsgObj.class;
                clazz = Object.class;
            } else if (msgType== POIMMsg.MsgType.ENDLIVE.type) {
                raw = POIMMsgObj.class;
                clazz = POIMEnd.class;
            } else if (msgType == POIMMsg.MsgType.GIFT.type) {
                raw = POIMMsgObj.class;
                clazz = POIMGift.class;
            } else if (msgType == POIMMsg.MsgType.LEVEL.type) {
                raw = POIMMsgObj.class;
                clazz = POIMLevel.class;
            } else if (msgType == POIMMsg.MsgType.DANMU.type) {
                raw = POIMMsgObj.class;
                clazz = POIMDanmu.class;
            } else if (msgType == POIMMsg.MsgType.INTERRUPTLIVE.type) {
                raw = POIMMsgObj.class;
                clazz = Object.class;
            } else if (msgType == POIMMsg.MsgType.LIKE.type) {
                raw = POIMMsgObj.class;
                clazz = POIMLike.class;
            } else if (msgType == POIMMsg.MsgType.USERLIST.type) {
                raw = POIMMsgUserList.class;
                clazz = POIMUser.class;
            } else if (msgType == POIMMsg.MsgType.WELCOME.type) {
                raw = POIMMsgObj.class;
                clazz = POIMWelcome.class;
            } else if (msgType == POIMMsg.MsgType.CONTENT.type) {
                raw = POIMMsgObj.class;
                clazz = POIMContent.class;
            } else if (msgType == POIMMsg.MsgType.RANKLIST.type) {
                raw = POIMMsgList.class;
                clazz = POIMRank.class;
            } else if (msgType == POIMMsg.MsgType.PERMISSION.type) {
                raw = POIMMsgObj.class;
                clazz = POIMPermission.class;
            }else if (msgType == POIMMsg.MsgType.ADMIN.type) {
                Log.d("liu","管理"+content);
                raw = POIMMsgObj.class;
                clazz = POIMAdmin.class;
            }else if(msgType == POIMMsg.MsgType.ATTENDANCHOR.type){
                raw = POIMMsgObj.class;
                clazz = POAttendanchor.class;
            }

            Type objectType = type(raw, clazz);
            return new Gson().fromJson(content, objectType);
        }catch (Exception e){
            Logger.e("samuel","直播间消息parse错误,content:"+content);
            e.printStackTrace();
        }
        return null;
    }


}
