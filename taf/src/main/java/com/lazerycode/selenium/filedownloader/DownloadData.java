package com.lazerycode.selenium.filedownloader;

/**
 * Class to hold the status &amp; the downloaded file
 */
public class DownloadData {
    private int status;
    private byte[] file;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

}
