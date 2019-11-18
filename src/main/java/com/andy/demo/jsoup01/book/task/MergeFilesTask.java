package com.andy.demo.jsoup01.book.task;

import com.andy.demo.jsoup01.book.INet;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;

/**
 * 合并文件 任务线程
 */
@Slf4j
public class MergeFilesTask implements Runnable {
    private static final int BUFFER_SIZE = 1024 * 100;

    private INet iNet;
    private CountDownLatch downLatch;

    public MergeFilesTask(INet iNet, CountDownLatch downLatch) {
        this.iNet = iNet;
        this.downLatch = downLatch;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        log.info("启用 合并文件 任务线程（等待所有下载任务结束）.");
        try {
            // 等待 所有下载任务线程 完毕
            this.downLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("所有下载任务已结束，开始合并小说.");
        String outFile = INet.DOWNLOAD_ADDRESS + iNet.bookName();
        File[] files = new File(outFile).listFiles();

        mergeFiles(outFile + ".txt", files);
    }

    /**
     * 合并文件
     *
     * @param outFile 合并后输出的文件名
     * @param files   将合并的文件集合
     */
    private void mergeFiles(String outFile, File[] files) {
        FileChannel outChannel = null;
        FileInputStream fileInputStream;

        FileChannel fc;
        ByteBuffer bb;
        try {
            outChannel = new FileOutputStream(outFile).getChannel();
            for (File f : files) {
                fileInputStream = new FileInputStream(f);
                fc = fileInputStream.getChannel();
                bb = ByteBuffer.allocate(BUFFER_SIZE);
                while (fc.read(bb) != -1) {
                    bb.flip();
                    outChannel.write(bb);
                    // 添加 小说阅读 目录标签
                    outChannel.write(ByteBuffer.wrap("\r\n\n".getBytes()));
                    bb.clear();
                }
                fc.close();
                fileInputStream.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException ignore) {
            }
        }

        log.info("合并完成");
    }
}
