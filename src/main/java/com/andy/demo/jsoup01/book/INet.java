package com.andy.demo.jsoup01.book;

import com.andy.demo.jsoup01.book.chapter.Chapter;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 小说网站 接口
 */
public interface INet {
    /**
     * 小说名称
     * @return
     */
    String bookName();

    /**
     * 小说 更新的所有章节集合
     * @return
     * @throws IOException
     */
    List<Chapter> getBooks() throws IOException;

    /**
     * 小说章节 内容
     * @param book
     * @throws IOException
     */
    void getContext(Chapter book) throws IOException;

    /**
     * 小说 保存地址
     */
    String DOWNLOAD_ADDRESS = "d:" + File.separator + "小说"
            + File.separator;

    /**
     * 每个线程下载的章节数，设置为 50
     */
    int CHAPTER_NUMBER = 50;
}
