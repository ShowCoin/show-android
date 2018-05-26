package one.show.live.upload.biz.avatarupload;


import java.util.HashMap;
import java.util.Map;

import one.show.live.common.po.POCommonResp;
import one.show.live.po.POConfig;
import one.show.live.po.PONew;
import one.show.live.upload.biz.CommonUploadInfo;
import one.show.live.upload.biz.UploadErrorCode;
import one.show.live.upload.exception.VODException;


/**
 * Created by samuel on 2018/5/5.
 * <p>
 * <p>
 * 个人头像上传 实体bean
 */

public class AvatarUploadInfo extends CommonUploadInfo {


    private PONew poNew;

    private boolean isSmall;

    public AvatarUploadInfo(String filePath,boolean isSmall) {
        this.filePath = filePath;
        this.isSmall = isSmall;
        setBucket(POConfig.getInstance().getAliyun_bucketname());
    }

    public PONew getPoNew() {
        return poNew;
    }

    public void setPoNew(PONew poNew) {
        this.poNew = poNew;
    }

    public boolean isSmall() {
        return isSmall;
    }

    public void setSmall(boolean small) {
        isSmall = small;
    }

    @Override
    protected int getTokenType() {
        return 5;
    }


    @Override
    public void postUpload() throws VODException {


        if(isSmall){//如果是小图的话就先存起来，等着和大图一起传
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("avatar", getSmallPath());
        params.put("large_avatar", getObject());

        POCommonResp<PONew> result = new UpdateUserInfoFormRequest().requestSync(params);

        if (result != null && result.isSuccess() && result.getData() != null) {
            setPoNew(result.getData());
        } else {
            throw new VODException(UploadErrorCode.ERROR_CODE_COMPLETEREQ, "应用服务器无响应");
        }

    }


}
