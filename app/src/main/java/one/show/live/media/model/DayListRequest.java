package one.show.live.media.model;

import android.os.Handler;
import android.os.Message;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import one.show.live.api.BaseBizRequest;
import one.show.live.common.po.POCommonResp;
import one.show.live.common.po.POListData;
import one.show.live.po.PODayList;


/**
 * Created by Nano on 2018/4/9.
 */

public abstract class DayListRequest extends BaseBizRequest<POListData<PODayList>> {
    public  int FOCUS = 0x1<<1;
    public  int LATEST = 0x1<<2;

    int type;



    String img1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523274301380&di=" +
            "9aae8b3ff1e5ee153eff508314f80c00&imgtype=0&src=http%3A%2F%2Fmvimg11.meitudata.com%2F57d2c25b9a07d6416.jpg";
    String img2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523274358432&di=10" +
            "8820a1c91430fc953565eddf5e6e98&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%3D580%2Fsign%3D880fbe3f5db5c9ea62f303ebe538b622%2F40c489b1cb134954b5b76050504e9258d1094a70.jpg";
    String img4 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523279135756&di=7466fa2bdf84d" +
            "07e7333b00b4e31bb69&imgtype=0&src=http%3A%2F%2Fimg0.utuku.china.com%2F590x0%2Fgame%2F20170620%2F81e8975f-a57e-49e0-bf13-66fc1a151780.png";
    String img5 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523279135754&di=c55eeecb9a46b19" +
            "344d09d7b2cda9c2a&imgtype=0&src=http%3A%2F%2Fupload.chinaz.com%2F2016%2F0122%2F1453452770976.jpg";

    public DayListRequest(int type){
        this.type = type;
    }
    /**
     * 固定服务器的path地址
     */
    @Override
    public String getPath() {
        return null;
    }



    /**
     * JSON解析
     *
     * @param result
     */
    @Override
    public void onRequestResult(String result) {
        Type type = new TypeToken<POCommonResp<POListData<PODayList>>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }
    POListData<PODayList> listP;
    @Override
    public void startRequest(Map<String, String> params) {
        listP = new POListData<>();
        listP.setNext_cursor(10);
        List<PODayList> poFocusList = new ArrayList<>();
        PODayList poFocus1 = new PODayList();
        if(type ==FOCUS){
            poFocus1.setNickName(img1);
        }else{
            poFocus1.setNickName(img4);
        }

        poFocusList.add(poFocus1);
        PODayList poFocus2 = new PODayList();
        if(type ==FOCUS){
            poFocus2.setNickName(img2);
        }else{
            poFocus2.setNickName(img5);
        }
        poFocusList.add(poFocus2);
        poFocusList.add(poFocus1);
        poFocusList.add(poFocus1);
        poFocusList.add(poFocus2);
        poFocusList.add(poFocus2);
        poFocusList.add(poFocus2);
        poFocusList.add(poFocus1);
        poFocusList.add(poFocus1);
        poFocusList.add(poFocus2);
        poFocusList.add(poFocus2);
        poFocusList.add(poFocus1);
        poFocusList.add(poFocus1);
        poFocusList.add(poFocus2);
        poFocusList.add(poFocus2);
        poFocusList.add(poFocus2);
        poFocusList.add(poFocus1);
        poFocusList.add(poFocus1);
        poFocusList.add(poFocus2);


        listP.setList(poFocusList);
        handler.sendEmptyMessage(0);

    }

    /**
     * 测试需要
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            onFinish(true,"",listP);
            return false;
        }
    });
}
