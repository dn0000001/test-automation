package com.taf.automation.locking;

import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Ensures exclusive use of registered users
 */
public class UserLockManager {
    private static final ReentrantLock action = new ReentrantLock();
    private static final Condition userRemoved = action.newCondition();
    private static final Map<String, Long> storedUsers = new HashMap<>();
    private static final Map<String, ReentrantLock> pendingUsers = new HashMap<>();

    private UserLockManager() {
        //
    }

    private static class LazyHolder {
        private static final UserLockManager INSTANCE = new UserLockManager();
    }

    public static UserLockManager getInstance() {
        return UserLockManager.LazyHolder.INSTANCE;
    }

    public Long lock(String user) {
        return lock(user, Thread.currentThread().getId());
    }

    public Long lock(String user, Long threadId) {
        action.lock();
        pendingUsers.putIfAbsent(user, new ReentrantLock());
        action.unlock();

        pendingUsers.get(user).lock();
        while (storedUsers.containsKey(user)) {
            try {
                userRemoved.await();
            } catch (Exception ex) {
                //
            }
        }

        action.lock();
        storedUsers.put(user, threadId);
        pendingUsers.get(user).unlock();
        action.unlock();

        return threadId;
    }

    public boolean unlock() {
        return unlock(Thread.currentThread().getId());
    }

    public boolean unlock(Long threadId) {
        action.lock();
        try {
            boolean result = storedUsers.values().removeIf(item -> item.equals(threadId));
            if (result) {
                userRemoved.signalAll();
            }

            return result;
        } finally {
            action.unlock();
        }
    }

    public Long unlock(String user) {
        action.lock();
        try {
            Long result = storedUsers.remove(user);
            userRemoved.signalAll();
            return result;
        } finally {
            action.unlock();
        }
    }

    /**
     * Get the user locked by the current thread
     *
     * @return null if no user being locked, else user
     */
    public String getUser() {
        return getUser(Thread.currentThread().getId());
    }

    /**
     * Get the user locked by the thread
     *
     * @param threadId - Thread Id to get the user being locked by the thread
     * @return null if no user being locked, else user
     */
    public String getUser(Long threadId) {
        for (Map.Entry<String, Long> item : storedUsers.entrySet()) {
            if (ObjectUtils.compare(item.getValue(), threadId) == 0) {
                return item.getKey();
            }
        }

        return null;
    }

}
