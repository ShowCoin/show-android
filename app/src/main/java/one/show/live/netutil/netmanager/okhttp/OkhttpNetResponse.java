package one.show.live.netutil.netmanager.okhttp;

import android.text.TextUtils;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import one.show.live.R;
import one.show.live.netutil.netmanager.common.BaseNetResponse;
import one.show.live.netutil.netmanager.common.NetStatusCode;
import one.show.live.netutil.okhttputils.OkHttpUtils;
import okhttp3.Response;


public class OkhttpNetResponse extends BaseNetResponse<Response, OkhttpNetResponse> {

    @Override
    public int getStatusCode() {
        if (getResponse() != null) {
            return getResponse().code();
        }
        return NetStatusCode.UNKNOWN_ERROR;
    }

    @Override
    public String getMessage() {
        if (!TextUtils.isEmpty(getServerMsg())) {
            return getServerMsg();
        }

        if (!TextUtils.isEmpty(getLocalMsg())) {
            return getLocalMsg();
        }

        if (!TextUtils.isEmpty(super.getMessage())) {
            return super.getMessage();
        }

        if (getException() instanceof SocketTimeoutException) {
            return OkHttpUtils.getContext().getString(R.string.message_socket_timeout);
        }

        if (getException() instanceof SSLHandshakeException) {

        }

        if (getException() != null) {
            return getMessage(getException());
        }

        return OkHttpUtils.getContext().getString(R.string.network_unknown_error);
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        if (getResponse() != null
                && getResponse().headers() != null) {

            return getResponse().headers().toMultimap();
        }
        return null;
    }

    @Override
    public String getContent() {
        if (super.getContent() == null) {
            try {
                if (getResponse() != null
                        && getResponse().body() != null) {
                    setContent(getResponse().body().string());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.getContent();
    }

    private String getMessage(Throwable e) {
        StringBuilder sb = new StringBuilder("");
        final String SEP = System.getProperty("line.separator");
        int count = 0;
        while (true) {
            sb.append("Caused by ");
            sb.append(e.getClass().getSimpleName());
            sb.append("(");
            sb.append(e.getMessage());
            sb.append(")");
            e = e.getCause();
            if (++count > 9 || e == null) {
                break;
            }
            sb.append(SEP);
        }
        return sb.toString();
    }

}
