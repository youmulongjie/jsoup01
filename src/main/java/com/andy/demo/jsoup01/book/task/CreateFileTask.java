package com.andy.demo.jsoup01.book.task;

import com.andy.demo.jsoup01.book.INet;
import com.andy.demo.jsoup01.book.chapter.Chapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 创建本地文件 任务线程
 */
@Slf4j
public class CreateFileTask implements Runnable {
    private INet iNet;
    private List<Chapter> books;
    private CountDownLatch downLatch;

    public CreateFileTask(INet iNet, List<Chapter> books, CountDownLatch downLatch) {
        this.iNet = iNet;
        this.books = books;
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
        log.info("当前线程：{}，下载章数：【{}~{}】，共{}章",
                Thread.currentThread().getName(),
                books.get(0).getIndex(),
                books.get(books.size() - 1).getIndex(),
                books.size());

        books.forEach((Chapter chapter) -> {
            try {
                iNet.getContext(chapter);
                write(chapter);
            } catch (IOException e) {
            }
        });
        // 计数 减一
        this.downLatch.countDown();
    }

    /**
     * 写文件
     *
     * @param chapter 小说章节
     * @throws IOException
     */
    private final void write(Chapter chapter) throws IOException {
        File directory = new File(INet.DOWNLOAD_ADDRESS + iNet.bookName());
        // 强制创建文件夹
        FileUtils.forceMkdir(directory);
        File file = new File(directory, index(chapter.getIndex()) + "-" + chapter.getTitle() + ".txt");
        // 强制创建文件
        FileUtils.touch(file);
        FileUtils.writeStringToFile(file, chapter.getContext(), "UTF-8");
    }

    /**
     * 以index作为小文件名称，便于合并大文件时排序
     *
     * @param index
     * @return
     */
    private final String index(Integer index) {
        DecimalFormat df = new DecimalFormat("0000");
        return df.format(index);
    }
}
