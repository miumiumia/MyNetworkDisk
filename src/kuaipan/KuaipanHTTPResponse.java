package kuaipan;

import kuaipan.*;

public class KuaipanHTTPResponse {

    public final static int KUAIPAN_OK = 200;
    public final static int KUAIPAN_LOGICAL_ERROR = 202;

    public final static int KUAIPAN_BAD_REQUEST_ERROR = 400;
    public final static int KUAIPAN_AUTHORIZATION_ERROR = 401;
    public final static int KUAIPAN_FOBBIDEN_ERROR = 403;
    public final static int KUAIPAN_NOT_FOUND_ERROR = 404;
    public final static int KUAIPAN_TOO_MANY_FILES_ERROR = 406;
    public final static int KUAIPAN_TOO_LARGE_ERROR = 413;

    public final static int KUAIPAN_SERVER_ERROR = 500;
    public final static int KUAIPAN_OVER_SPACE_ERROR = 507;
    public final static int KUAIPAN_UNKNOWNED_ERROR = 578;

    public final static String MSG_CONSUMER_EXPIRED = "bad consumer key";
    public final static String MSG_AUTHORIZATION_EXPIRED = "authorization expired";

    public int code = KUAIPAN_UNKNOWNED_ERROR;
    public String content;
    public KuaipanURL url;

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("code=");
        buf.append(Integer.toString(code));
        buf.append("\n");
        if (content != null) {
            buf.append("body=");
            buf.append(content);
            buf.append("\n");
        }
        return buf.toString();
    }

    public <T> T fromJson(Class<T> classOfT) throws KuaipanException {
        if (code == KuaipanHTTPResponse.KUAIPAN_OK) {
            return JsonUtil.fromJson(content, classOfT);
        } else {
            if (code == KuaipanHTTPResponse.KUAIPAN_AUTHORIZATION_ERROR) {
                ErrorMessage result = JsonUtil.fromJson(content, ErrorMessage.class);
                if (result != null) {
                    if (result.msg != null
                            && result.msg.equals(KuaipanHTTPResponse.MSG_AUTHORIZATION_EXPIRED))
                        throw new KuaipanAuthExpiredException();
                }
            }

            throw new KuaipanServerException(this);
        }
    }
}
