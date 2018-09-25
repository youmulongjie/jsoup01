package com.andy.demo.jsoup01.book.task;

import com.andy.demo.jsoup01.book.INet;
import com.andy.demo.jsoup01.book.chapter.Chapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
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
        log.info("下载章节数：{}", books.size());
        books.forEach((Chapter chapter) -> {
            try {
                iNet.getContext(chapter);
                write(chapter.getIndex(), chapter.getContext());
            } catch (IOException e) {
            }
        });
        // 计数 减一
        this.downLatch.countDown();
    }

    /**
     * 写文件
     * @param index 以index为名的文件名称
     * @param context 文件内容
     * @throws IOException
     */
    private final void write(Integer index, String context) throws IOException {
        File directory = new File(INet.DOWNLOAD_ADDR + iNet.bookName());
        // 强制创建文件夹
        FileUtils.forceMkdir(directory);
        File file = new File(directory, index(index) + ".txt");
        // 强制创建文件
        FileUtils.touch(file);
        FileUtils.writeStringToFile(file, context, "UTF-8");
    }

    /**
     *  小文件名称，便于合并大文件
     * @param index
     * @return
     */
    private  final String index(Integer index){
        if(index < 10){
            return "000" + index;
        } else if(index >= 10 && index < 100){
            return "00" + index;
        } else if(index >= 100 && index < 1000){
            return  "0" + index;
        } else {
            return index+"";
        }
    }
}
