package com.taf.automation.locking;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class provides an inter process basic mutex.  The main goal of this class is to allow multiple java processes
 * that need to write to the same file without losing any changes.  For example, all your automation Jenkins jobs
 * want to write something to a common file for the Jenkins slave.
 */
public class InterProcessBasicMutex {
    private static final ReentrantLock access = new ReentrantLock(true);
    private static final String DEFAULT_LOCK_FILE = "automation.lock";
    private RandomAccessFile randomAccessFile;
    private FileChannel channel;
    private FileLock lock;
    private boolean debuggingInfo;
    private boolean exclusiveAccess;

    private String directory;
    private String lockFile;

    public InterProcessBasicMutex() {
        withEnabledThreadSafety();
        withTempDirectory();
        withDefaultLockFile();
        withDebuggingInfoDisabled();
    }

    /**
     * Enable thread safety if the same lock file will/could be used by multiple threads.<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>This flag prevents the OverlappingFileLockException from occurring.</LI>
     * <LI>There could be a performance impact if there are many uses of this class</LI>
     * </OL>
     *
     * @return InterProcessBasicMutex
     */
    public InterProcessBasicMutex withEnabledThreadSafety() {
        exclusiveAccess = true;
        return this;
    }

    /**
     * Disable thread safety if only single thread and/or different lock files are used for each thread.<BR>
     * <B>Note: </B>
     * <OL>
     * <LI>Based on use of class with this flag, it is possible to get an OverlappingFileLockException.
     * It is up to the developer to handle/prevent this.</LI>
     * <LI>If there are many uses of this class, then the performance will/should be better.</LI>
     * </OL>
     *
     * @return InterProcessBasicMutex
     */
    public InterProcessBasicMutex withDisabledThreadSafety() {
        exclusiveAccess = false;
        return this;
    }

    public InterProcessBasicMutex withTempDirectory() {
        directory = FileUtils.getTempDirectory().getAbsolutePath();
        return this;
    }

    public InterProcessBasicMutex withUserDirectory() {
        this.directory = FileUtils.getUserDirectory().getAbsolutePath();
        return this;
    }

    public InterProcessBasicMutex withWorkingDirectory() {
        this.directory = new File(System.getProperty("user.dir")).getAbsolutePath();
        return this;
    }

    public InterProcessBasicMutex withDefaultLockFile() {
        lockFile = DEFAULT_LOCK_FILE;
        return this;
    }

    public InterProcessBasicMutex withLockFile(String filename) {
        lockFile = filename;
        return this;
    }

    public InterProcessBasicMutex withDirectory(String directory) {
        this.directory = StringUtils.defaultString(directory);
        return this;
    }

    public InterProcessBasicMutex withDebuggingInfoEnable() {
        debuggingInfo = true;
        return this;
    }

    public InterProcessBasicMutex withDebuggingInfoDisabled() {
        debuggingInfo = false;
        return this;
    }

    private RandomAccessFile getRandomAccessFile() {
        if (randomAccessFile == null) {
            try {
                randomAccessFile = new RandomAccessFile(FilenameUtils.concat(directory, lockFile), "rw");
            } catch (FileNotFoundException e) {
                if (debuggingInfo) {
                    e.printStackTrace();
                }
            }
        }

        return randomAccessFile;
    }

    @SuppressWarnings("java:S2259")
    private FileChannel getFileChannel() {
        if (channel == null) {
            channel = getRandomAccessFile().getChannel();
        }

        return channel;
    }

    private void lockAccess() {
        if (exclusiveAccess) {
            access.lock();
        }
    }

    private void unlockAccess() {
        if (exclusiveAccess && access.isHeldByCurrentThread()) {
            access.unlock();
        }
    }

    /**
     * @return true if lock was acquired successfully otherwise false
     */
    public boolean lock() {
        try {
            lockAccess();
            lock = getFileChannel().lock();
            return true;
        } catch (NullPointerException npe) {
            // the file channel was null which means the random access file threw an exception
            // which is already output provided debugging was enabled
        } catch (IOException e) {
            if (debuggingInfo) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * @return true if the lock was released successfully otherwise false
     */
    public boolean release() {
        if (lock != null) {
            try {
                if (lock.isValid()) {
                    lock.release();
                }
            } catch (IOException e) {
                if (debuggingInfo) {
                    e.printStackTrace();
                }

                return false;
            } finally {
                unlockAccess();
            }
        }

        unlockAccess();
        return true;
    }

}
