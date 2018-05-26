package one.show.live.upload.biz;


import one.show.live.common.po.POCommonResp;
import one.show.live.po.POUploadToken;
import one.show.live.upload.common.StringUtil;
import one.show.live.upload.exception.VODException;
import one.show.live.upload.model.BizUploadModelInterface;
import one.show.live.upload.model.UploadFileInfo;

/**
 * Created by samuel on 2018/4/25.
 *
 * 上传实体bean，上传前默认实现getToken
 */
public abstract class CommonUploadInfo extends UploadFileInfo implements BizUploadModelInterface {


    @Override
    public void preUpload() throws VODException {

        if (StringUtil.isEmpty(filePath)) {
            throw new VODException(UploadErrorCode.ERROR_CODE_FILENOTEXIST, "文件不存在");
        }

        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);

        POCommonResp<POUploadToken> result = new SyncUploadTokenRequest().getToken(ext, getTokenType());

        if (result != null && result.isSuccess() && result.getData() != null) {
            POUploadToken tokenObj = result.getData();
            setObject(tokenObj.getFileName());
        } else {
            throw new VODException(UploadErrorCode.ERROR_CODE_GETFILENAME, "获取文件名失败");
        }

    }


    /**
     * 不同类型上传文件之前，获取token需要指明当前是上传什么类型文件。
     * @return
     */
    protected abstract int getTokenType();


}
