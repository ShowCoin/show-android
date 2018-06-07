package one.show.live.streamer;

import android.content.Context;
import android.text.TextUtils;

import com.magic.sample.util.AssetsUtil;
import com.magic.sample.util.FilePathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by clarkM1ss1on on 2018/6/4
 */
public enum MKShaderHelper {

    INSTANCE;
    private final static String MK_ROOT_FOLDER = "magic";

    private final static String RAW_FILE_FOLDER_SHADER = "shader";
    private final static String RAW_FILE_FOLDER_FRAMES = "frames";

    private String mRootEnginePath;
    private Context context;
    private List<String> mTargetSearchPaths;
    private boolean isCopySucceed;

    public void init(Context ctx) {
        context = ctx
                .getApplicationContext();
        mTargetSearchPaths = new ArrayList<>();
    }

    public void copyDependencyRawFiles() {

        Observable.just(RAW_FILE_FOLDER_SHADER, RAW_FILE_FOLDER_FRAMES).map(new Func1<String, String>() {
            @Override
            public String call(String assetsName) {
                String targetPath = getEngineRootPath() + assetsName + File.separator;
                File targetFile = new File(targetPath);

                if (!targetFile.exists()) {
                    AssetsUtil.copyAssets(context, assetsName
                            , getEngineRootPath() + File.separator + assetsName);
                }

                return targetPath;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).toList().subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> arrays) {
                isCopySucceed = true;
                mTargetSearchPaths.addAll(arrays);
                mTargetSearchPaths.add(getEngineRootPath());

            }
        });

    }

    private String getEngineRootPath() {
        if (TextUtils.isEmpty(mRootEnginePath)) {
            mRootEnginePath = FilePathUtil.makeFilePath(context,
                    context.getDir(MK_ROOT_FOLDER, Context.MODE_PRIVATE).getAbsolutePath(), null);
        }
        return mRootEnginePath;
    }

    public boolean isCopySucceed() {
        return isCopySucceed;
    }

    public List<String> getTargetSearchPath() {
        return mTargetSearchPaths;
    }
}
