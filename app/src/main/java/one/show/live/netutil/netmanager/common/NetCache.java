package one.show.live.netutil.netmanager.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;


public class NetCache {

    private Map<Object, Set<BaseRequest>> CACHE_POOL = new WeakHashMap<>();

    public void addRequest(BaseRequest baseRequest) {
        if (baseRequest != null && baseRequest.getTag() != null) {
            Set<BaseRequest> requestSet = CACHE_POOL.get(baseRequest.getTag());
            if (requestSet == null) {
                requestSet = new HashSet<>();
                CACHE_POOL.put(baseRequest.getTag(), requestSet);
            }
            requestSet.add(baseRequest);
        }
    }

    public void cancelRequests(Object tag) {
        if (tag != null) {
            Set<BaseRequest> requestSet = CACHE_POOL.remove(tag);
            if (requestSet != null) {
                for (BaseRequest baseRequest : requestSet) {
                    if (baseRequest != null && !baseRequest.isCanceled() && !baseRequest.isFinished()) {
                        baseRequest.cancel();
                    }
                }
            }
        }
    }

    public void removeRequest(BaseRequest baseRequest) {
        if (baseRequest != null && baseRequest.getTag() != null) {
            Set<BaseRequest> requestSet = CACHE_POOL.get(baseRequest.getTag());
            if (requestSet != null) {
                requestSet.remove(baseRequest);
                if (requestSet.size() == 0) {
                    CACHE_POOL.remove(baseRequest.getTag());
                }
            }
        }
    }

    public void cancelRequest(BaseRequest baseRequest) {
        if (baseRequest != null && baseRequest.getTag() != null) {
            baseRequest.cancel();
            Set<BaseRequest> requestSet = CACHE_POOL.get(baseRequest.getTag());
            if (requestSet != null) {
                requestSet.remove(baseRequest);
                if (requestSet.size() == 0) {
                    CACHE_POOL.remove(baseRequest.getTag());
                }
            }
        }
    }

}
