package one.show.live.upload.exception;

public class VODClientException extends RuntimeException {
    private String code;
    private String message;

    public VODClientException() {
    }

    public VODClientException(String code, String message) {
        super("[ErrorCod]: " + code + ",[ErrorMessage]: " + message);
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        String base = super.getMessage();
        return this.getCause() == null?base:this.getCause().getMessage() + "\n" + base;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}