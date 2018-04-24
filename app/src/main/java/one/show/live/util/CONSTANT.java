package one.show.live.util;


import android.os.Environment;

/**
 * Created by wyc
 */

public class CONSTANT {

    public static final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String cacheName = "/show";
    public static final String download = cacheName + "/download";
    public static final String downloadVideo = download + "/video";
    public static final String downloadPicture = download + "/picture";
    public static final String argame = cacheName + "/argame";
    public static final String magicGame = cacheName + "/magicgame";
    public static final String argameRes = argame + "/gameRes";
    public static final String argameRes_copy = "/gameRes";
    public static final String argameModel = argame + "/gameModel";

    public static final int COMMENT_LIST_TYPE = 0;
    public static final int COMMENT_DIALOG_TYPE = 1;





}
