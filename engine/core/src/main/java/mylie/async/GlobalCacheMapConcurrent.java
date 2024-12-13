package mylie.async;

import java.util.concurrent.ConcurrentHashMap;

public class GlobalCacheMapConcurrent extends GlobalCacheMap {
    public GlobalCacheMapConcurrent() {
        super(new ConcurrentHashMap<>());
    }
}
