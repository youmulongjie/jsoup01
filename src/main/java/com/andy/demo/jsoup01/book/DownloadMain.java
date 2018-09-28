package com.andy.demo.jsoup01.book;

import com.andy.demo.jsoup01.book.chapter.Chapter;
import com.andy.demo.jsoup01.book.task.CreateFileTask;
import com.andy.demo.jsoup01.book.task.MergeFilesTask;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 小说网站 主下载类
 */
@Slf4j
public class DownloadMain {
    private INet iNet;

    public DownloadMain(INet iNet) {
        this.iNet = iNet;
    }

    /**
     * 下载
     */
    public final void download() {
        try {
            List<Chapter> books = iNet.getBooks();
            if (null == books || books.size() == 0) {
                log.info("【{}】共更新【{}】章", iNet.bookName(), 0);
                return;
            }
            log.info("【{}】共更新【{}】章", iNet.bookName(), books.size());

            ExecutorService executorService = Executors.newCachedThreadPool();

            // 启用下载线程的个数
            int c = books.size() % INet.CHAPTER_NUMBER == 0 ? books.size()
                    / INet.CHAPTER_NUMBER : books.size() / INet.CHAPTER_NUMBER
                    + 1;
            log.info("启用【{}】个 下载线程", c);

            CountDownLatch downLatch = new CountDownLatch(c);
            CreateFileTask createFile;
            for (int i = 0; i < c; i++) {
                if (i != c - 1) {
                    createFile = new CreateFileTask(iNet, books.subList(i
                            * INet.CHAPTER_NUMBER, (i + 1)
                            * INet.CHAPTER_NUMBER), downLatch);
                } else {
                    // 最后一次 遍历剩余的
                    createFile = new CreateFileTask(iNet, books.subList(i
                            * INet.CHAPTER_NUMBER, books.size()), downLatch);
                }
                executorService.execute(createFile);

            }

            MergeFilesTask mergeFilesTask = new MergeFilesTask(iNet, downLatch);
            executorService.execute(mergeFilesTask);

            // 启动一次顺序关闭，执行以前提交的任务，但不接受新任务
            executorService.shutdown();

            // 最多等待60秒
            executorService.awaitTermination(60 * 1000, TimeUnit.SECONDS);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
