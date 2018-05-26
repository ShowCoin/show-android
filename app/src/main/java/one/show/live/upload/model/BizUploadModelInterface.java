package one.show.live.upload.model;


import one.show.live.upload.exception.VODException;

/**
 * Created by samuel on 2018/5/5.
 *
 * 如果需要在上传的前后加入自己的业务逻辑，并且是耗时的操作，可以实现此接口
 */
public interface BizUploadModelInterface {

    /**
     * 上传前执行的方法，此方法在工作线程内执行
     * @throws VODException
     */
    void preUpload() throws VODException;

    /**
     * 上传结束后执行的方法，此方法在工作线程内执行
     * @throws VODException
     */
    void postUpload() throws VODException;
}
