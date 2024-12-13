package mylie.async;

import java.util.HashMap;
import java.util.concurrent.locks.StampedLock;

public class GlobalCacheStampedLock extends GlobalCacheMap {
    private final StampedLock lock = new StampedLock();

    public GlobalCacheStampedLock() {
        super(new HashMap<>());
    }

    @Override
    public <T> Result<T> get(int hash) {
        long stamp = lock.tryOptimisticRead();
        Result<T> objectResult = super.get(hash);
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                objectResult = super.get(hash);
            } finally {
                lock.unlockRead(stamp);
            }
        }

        return objectResult;
    }

    @Override
    public <T> void set(int hash, Result<T> result) {
        long stamp = lock.writeLock();
        super.set(hash, result);
        lock.unlockWrite(stamp);
    }

    @Override
    public void remove(int hash) {
        long stamp = lock.writeLock();
        super.remove(hash);
        lock.unlockWrite(stamp);
    }
}
