package one.show.live.upload.internal;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import one.show.live.BuildConfig;
import one.show.live.MeetApplication;
import one.show.live.api.BaseAPI;
import one.show.live.api.BaseRequestSync;
import one.show.live.common.po.POCommonResp;
import one.show.live.common.util.StringUtils;
import one.show.live.po.POAliyunConfig;
import one.show.live.po.POConfig;


/**
 * Created by samuel on 2018/5/5.
 */

public class BuildAliyunClientUtil {

    private static OSSClient ossClient = null;
    private static Object lockObj = new Object();

    public static OSSClient buildClient() {
        if (ossClient == null) {
            synchronized (lockObj) {
                if (ossClient == null) {
                    OSSCredentialProvider credentialProvider = new OSSFederationCredentialProvider() {
                        @Override
                        public OSSFederationToken getFederationToken() {
                            try {
                                POCommonResp<POAliyunConfig> data = new BaseRequestSync<POAliyunConfig>() {
                                    @Override
                                    public void onRequestResult(String result) {
                                        Type type = new TypeToken<POCommonResp<POAliyunConfig>>() {
                                        }.getType();
                                        responseBean = new Gson().fromJson(result, type);
                                    }


                                    @Override
                                    public String getPath() {
                                        return BaseAPI.Path.getAliyunConfig;
                                    }

                                }.requestSync(null);

                                if(data!=null&&data.isSuccess()&&data.getData()!=null) {
                                    POAliyunConfig config = data.getData();
                                    return new OSSFederationToken(config.getAccessKeyId(), config.getAccessKeySecret(), config.getSecurityToken(), config.getExpiration());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    };

                    String endpoint = POConfig.getInstance().getAliyun_endpoint();

                    if(StringUtils.isNotEmpty(endpoint)) {
                        ClientConfiguration conf = new ClientConfiguration();
                        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
                        conf.setMaxErrorRetry(0); // 失败后最大重试次数，默认0次

                        if (BuildConfig.IS_DEBUG) {
                            OSSLog.enableLog();
                        } else {
                            OSSLog.disableLog();
                        }

                        ossClient = new OSSClient(MeetApplication.getContext(), endpoint, credentialProvider, conf);
                    }

                }
            }
        }
        return ossClient;
    }




}
