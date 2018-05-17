//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package one.show.live.upload.internal;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.PartETag;
import com.alibaba.sdk.android.oss.model.UploadPartRequest;
import com.alibaba.sdk.android.oss.model.UploadPartResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import one.show.live.upload.common.UploadStateType;
import one.show.live.upload.model.BizUploadModelInterface;
import one.show.live.upload.model.UploadFileInfo;


public class OSSUploaderImpl implements OSSUploader {

    private static final long LARGE_FILE_LENGTH = 134217728L;
    private static final int SMALL_BLOCK_SIZE = 262144;
    private static final int LARGE_BLOCK_SIZE = 524288;
    private OSSUploadListener listener;
    private OSS oss;
    private Context context;
    private UploadFileInfo uploadFileInfo;


    private String uploadId;
    private long uploadedSize = -1;
    private InputStream inputStream;
    private File file;

    public OSSUploaderImpl(Context context) {
        this.context = context;
    }

    @Override
    public void init(OSSUploadListener listener) {
        OSSLog.logD("[OSSUploader] - init...");
        this.listener = listener;
        this.oss = BuildAliyunClientUtil.buildClient();
    }

    @Override
    public void start(UploadFileInfo uploadFileInfo) {

        if (null != this.uploadFileInfo && !uploadFileInfo.equals(this.uploadFileInfo)) {
            uploadFileInfo.setStatus(UploadStateType.INIT);
        }

        OSSLog.logD("[OSSUploader] - start..." + uploadFileInfo.getFilePath());

        this.uploadFileInfo = uploadFileInfo;
        uploadFileInfo.setStatus(UploadStateType.UPLOADING);
        try {
            if (uploadFileInfo instanceof BizUploadModelInterface) {
                ((BizUploadModelInterface) uploadFileInfo).preUpload();
            }
            startUpload();
            if (uploadFileInfo instanceof BizUploadModelInterface) {
                ((BizUploadModelInterface) uploadFileInfo).postUpload();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.uploadFileInfo.setStatus(UploadStateType.FAIlURE);
            //FIXME:  不同异常可以提示不同信息
            this.listener.onUploadFailed("Exception", "上传失败");
            return;
        }

        uploadFileInfo.setStatus(UploadStateType.SUCCESS);
        this.listener.onUploadSucceed();

    }

    @Override
    public void cancel() {
        if (null != this.uploadFileInfo) {
            UploadStateType status = this.uploadFileInfo.getStatus();
            if (!UploadStateType.INIT.equals(status) && !UploadStateType.UPLOADING.equals(status)) {
                OSSLog.logD("[OSSUploader] - status: " + status + " cann\'t be cancel!");
            } else {
                OSSLog.logD("[OSSUploader] - cancel...");
                this.uploadFileInfo.setStatus(UploadStateType.CANCELED);
            }
        }
    }

    private void abortUpload() {
        if (this.uploadId != null) {
            try {
                AbortMultipartUploadRequest e = new AbortMultipartUploadRequest(this.uploadFileInfo.getBucket(), this.uploadFileInfo.getObject(), this.uploadId);
                this.oss.abortMultipartUpload(e);
                if (inputStream != null) {
                    this.inputStream.close();
                }
            } catch (ClientException e) {
                OSSLog.logW("[OSSUploader] - abort ClientException!code:" + e.getCause() + ", message:" + e.getMessage());
            } catch (ServiceException e) {
                OSSLog.logW("[OSSUploader] - abort ServiceException!code:" + e.getCause() + ", message:" + e.getMessage());
            } catch (IOException e) {
                OSSLog.logW("[OSSUploader] - abort IOException!code:" + e.getCause() + ", message:" + e.getMessage());
            }
        }
    }


    /**
     * 分片上传
     *
     * @throws ClientException
     * @throws ServiceException
     * @throws IOException
     */
    private void startUpload() throws ClientException, ServiceException, IOException {

        InitiateMultipartUploadRequest ossRequest = new InitiateMultipartUploadRequest(this.uploadFileInfo.getBucket(), this.uploadFileInfo.getObject());
        InitiateMultipartUploadResult initResult = this.oss.initMultipartUpload(ossRequest);

        uploadId = initResult.getUploadId();

        List<PartETag> uploadedParts = new ArrayList();

        int currentIndex = 1;

        this.file = new File(uploadFileInfo.getFilePath());

        inputStream = new FileInputStream(file);

        long fileLength = file.length();
        uploadedSize = 0;

        int blockSize;

        if (fileLength < LARGE_FILE_LENGTH) {
            blockSize = SMALL_BLOCK_SIZE;
        } else {
            blockSize = LARGE_BLOCK_SIZE;
        }

        while (uploadedSize < fileLength) {

            if (UploadStateType.CANCELED.equals(this.uploadFileInfo.getStatus())) {
                OSSUploaderImpl.this.abortUpload();
                OSSUploaderImpl.this.listener.onUploadFailed(UploadStateType.CANCELED.toString(), "This task is cancelled!");
                OSSLog.logD("[OSSUploader] - This task is cancelled!");
                return;
            }

            int partLength = (int) Math.min(blockSize, fileLength - uploadedSize);

            byte[] partData = IOUtils.readStreamAsBytesArray(inputStream, partLength);

            UploadPartRequest uploadPart = new UploadPartRequest(this.uploadFileInfo.getBucket(), this.uploadFileInfo.getObject(), uploadId, currentIndex);
            uploadPart.setPartContent(partData);
            uploadPart.setProgressCallback(new OSSProgressCallback<UploadPartRequest>() {

                public void onProgress(UploadPartRequest uploadPartRequest, long l, long l1) {
                    OSSUploaderImpl.this.listener.onUploadProgress(uploadedSize + l, file.length());
                }
            });

            UploadPartResult uploadPartResult = oss.uploadPart(uploadPart);

            uploadedParts.add(new PartETag(currentIndex, uploadPartResult.getETag()));

            uploadedSize += partLength;

            currentIndex++;
        }


        CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(this.uploadFileInfo.getBucket(), this.uploadFileInfo.getObject(), uploadId, uploadedParts);
//        ObjectMetadata metadata = request.getMetadata();
//        if (metadata == null) {
//            metadata = new ObjectMetadata();
//        }
//
//        if (null != this.uploadFileInfo.getVodInfo()) {
//            metadata.addUserMetadata("x-oss-notification", this.uploadFileInfo.getVodInfo().toVodJsonStringWithBase64());
//        }
//
//        request.setMetadata(metadata);

        oss.completeMultipartUpload(request);

        inputStream.close();
        inputStream = null;

    }
//    public OSSUploadRetryType shouldRetry(Exception e) {
//        if (e instanceof ClientException) {
//            Exception serviceException1 = (Exception) e.getCause();
//            if (serviceException1 instanceof InterruptedIOException && !(serviceException1 instanceof SocketTimeoutException)) {
//                OSSLog.logE("[shouldNotetry] - is interrupted!");
//                return OSSUploadRetryType.ShouldNotRetry;
//            } else if (serviceException1 instanceof IllegalArgumentException) {
//                return OSSUploadRetryType.ShouldNotRetry;
//            } else {
//                OSSLog.logD("shouldRetry - " + e.toString());
//                e.getCause().printStackTrace();
//                return OSSUploadRetryType.ShouldRetry;
//            }
//        } else if (e instanceof ServiceException) {
//            ServiceException serviceException = (ServiceException) e;
//            return serviceException.getErrorCode() != null &&
//                    serviceException.getErrorCode().equalsIgnoreCase("RequestTimeTooSkewed") ? OSSUploadRetryType.ShouldRetry : (serviceException.getStatusCode() >= 500 ?
//                    OSSUploadRetryType.ShouldRetry : OSSUploadRetryType.ShouldNotRetry);
//        } else {
//            return OSSUploadRetryType.ShouldNotRetry;
//        }
//    }
//
//
//        public void onFailure(OSSRequest request, ClientException clientException, ServiceException serviceException) {
//            UploadStateType status = OSSUploaderImpl.this.uploadFileInfo.getStatus();
//            Object exception = null;
//            if (clientException != null) {
//                exception = clientException;
//            } else if (serviceException != null) {
//                exception = serviceException;
//            }
//
//            if (exception == null) {
//                OSSLog.logE("onFailure error: exception is null.");
//            } else {
//                switch (OSSUploaderImpl.this.shouldRetry((Exception) exception).ordinal()) {
//                    case 1:
//                        if (UploadStateType.PAUSING.equals(status)) {
//                            OSSLog.logD("[OSSUploader] - This task is pausing!");
//                            OSSUploaderImpl.this.uploadFileInfo.setStatus(UploadStateType.PAUSED);
//                            return;
//                        }
//
//                        try {
//                            Thread.sleep(3000L);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        if (request instanceof InitiateMultipartUploadRequest) {
//                            OSSUploaderImpl.this.oss.asyncInitMultipartUpload((InitiateMultipartUploadRequest) OSSUploaderImpl.this.ossRequest, OSSUploaderImpl.this.initCallback);
//                        } else if (request instanceof CompleteMultipartUploadRequest) {
//                            OSSUploaderImpl.this.oss.asyncCompleteMultipartUpload((CompleteMultipartUploadRequest) OSSUploaderImpl.this.ossRequest, OSSUploaderImpl.this.completedCallback);
//                        } else if (request instanceof UploadPartRequest) {
//                            OSSUploaderImpl.this.oss.asyncUploadPart((UploadPartRequest) OSSUploaderImpl.this.ossRequest, OSSUploaderImpl.this.partCallback);
//                        }
//
//                        if (OSSUploaderImpl.this.retryShouldNotify) {
//                            if (clientException != null) {
//                                OSSUploaderImpl.this.listener.onUploadRetry("ClientException", clientException.toString());
//                            } else if (serviceException != null) {
//                                OSSUploaderImpl.this.listener.onUploadRetry(serviceException.getErrorCode(), serviceException.getMessage());
//                            }
//
//                            OSSUploaderImpl.this.retryShouldNotify = false;
//                        }
//                        break;
//                    case 2:
//                        OSSUploaderImpl.this.uploadFileInfo.setStatus(UploadStateType.PAUSED);
//                        OSSUploaderImpl.this.listener.onUploadTokenExpired();
//                        break;
//                    case 3:
//                        OSSUploaderImpl.this.uploadFileInfo.setStatus(UploadStateType.FAIlURE);
//                        if (clientException != null) {
//                            OSSUploaderImpl.this.listener.onUploadFailed("ClientException", clientException.toString());
//                        } else if (serviceException != null) {
//                            OSSUploaderImpl.this.listener.onUploadFailed(serviceException.getErrorCode(), serviceException.getMessage());
//                        }
//                }
//
//            }
//        }
//    }
}
