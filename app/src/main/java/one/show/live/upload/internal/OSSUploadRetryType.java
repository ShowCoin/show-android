package one.show.live.upload.internal;

public enum OSSUploadRetryType {
    ShouldNotRetry,
    ShouldRetry,
    ShouldGetSTS;

    private OSSUploadRetryType() {
    }
}
