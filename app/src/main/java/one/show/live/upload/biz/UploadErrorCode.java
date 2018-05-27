package one.show.live.upload.biz;

/**
 * Created by samuel on 2018/5/5.
 */

public class UploadErrorCode {
    /**
     * 文件找不到
     */
    public static final String ERROR_CODE_FILENOTEXIST = "-1";
    /**
     * getToken失败
     */
    public static final String ERROR_CODE_GETFILENAME = "-2";
    /**
     * 上传完成后，提交服务器失败
     */
    public static final String ERROR_CODE_COMPLETEREQ ="-3";

    /**
     * 作品描述敏感词或不合法
     */
    public static final String ERROR_CODE_ERRORTEXT ="-4";

}
