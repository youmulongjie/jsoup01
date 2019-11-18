package com.andy.demo.jsoup01.book;

import com.andy.demo.jsoup01.book.chapter.Chapter;
import com.andy.demo.jsoup01.book.exception.BookNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 小说网站 接口
 */
public interface INet {
    /**
     * 没有搜索到小说 标识
     */
    String BOOK_NOT_FOUND = "-1";

    /**
     * 每个线程下载的章节数，设置为 50
     */
    int CHAPTER_NUMBER = 50;

    /**
     * 是否检索到小说
     *
     * @return
     */
    boolean isFound();

    /**
     * 小说名称
     *
     * @return 小说名称
     */
    String bookName();

    /**
     * 小说索引
     *
     * @return 小说在网站中的索引标号
     */
    String bookIndex() throws BookNotFoundException;

    /**
     * 小说 更新的所有章节集合
     *
     * @return 小说更新过的章节
     * @throws IOException
     */
    List<Chapter> chapterList() throws Exception;

    /**
     * 小说章节 内容
     *
     * @param chapter
     * @throws IOException
     */
    void getContext(Chapter chapter) throws IOException;

    /**
     * 小说 保存地址
     */
    String DOWNLOAD_ADDRESS = "d:" + File.separator + "小说"
            + File.separator;

}
