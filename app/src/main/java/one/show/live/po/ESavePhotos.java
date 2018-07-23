package one.show.live.po;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;

import one.show.live.common.po.POCover;
import one.show.live.util.PublicCacheManager;


/**
 * Created by apple on 16/6/19.
 */
public class ESavePhotos implements Serializable {

    private List<POCover> list;

    private static ESavePhotos mInstanceEsavePhoto;

    private POCover choosePOCover;//选中的封面

    public void sendEvent() {
        EventBus.getDefault().post(this);
    }

    public List<POCover> getList() {
        return list;
    }

    public void setList(List<POCover> list) {
        this.list = list;
    }

    public POCover getChoosePOCover() {
        return choosePOCover;
    }

    public void setChoosePOCover(POCover choosePOCover) {
        this.choosePOCover = choosePOCover;
    }
//////////////////////////////////////////////////////////////


    /**
     * 获取封面信息
     */
    public static ESavePhotos getInstance() {
        if (mInstanceEsavePhoto == null) {
            synchronized (ESavePhotos.class) {
                if (mInstanceEsavePhoto == null) {
                    mInstanceEsavePhoto = new Gson().fromJson(PublicCacheManager.getInstance().getValue("ESavePhotos", "{}"), ESavePhotos.class);
                }
            }
        }

        if (mInstanceEsavePhoto == null) {
            return new ESavePhotos();
        }
        return mInstanceEsavePhoto;
    }

    /**
     * 保存数据
     *
     * @param eSavePhotos
     */
    public static void save(ESavePhotos eSavePhotos) {

        PublicCacheManager.getInstance().setValue("ESavePhotos", new Gson().toJson(eSavePhotos));
        mInstanceEsavePhoto = eSavePhotos;

        for (int i = 0; i < eSavePhotos.getList().size(); i++) {
            if (eSavePhotos.getList().get(i).getIsSelected() == 1) {
                fixCover(eSavePhotos.getList().get(i));
            }
        }
    }

    /**
     * 修改当前选中的cover
     * @param poCover
     */
    public static void fixCover(POCover poCover) {
        ESavePhotos eSavePhotos = getInstance();
        eSavePhotos.setChoosePOCover(poCover);
        PublicCacheManager.getInstance().setValue("ESavePhotos", new Gson().toJson(eSavePhotos));
        mInstanceEsavePhoto = eSavePhotos;
    }

    /**
     * 修改选中的开播封面
     * @param position
     */
    public static void fixChoose(int position) {

        ESavePhotos eSavePhotos = getInstance();


        for (int i = 0; i < eSavePhotos.getList().size(); i++) {
            if (position == i) {
                eSavePhotos.getList().get(i).setIsSelected(1);
                fixCover(eSavePhotos.getList().get(i));
            } else {

                eSavePhotos.getList().get(i).setIsSelected(0);
            }
        }

        PublicCacheManager.getInstance().setValue("ESavePhotos", new Gson().toJson(eSavePhotos));
        mInstanceEsavePhoto = eSavePhotos;
    }
}
