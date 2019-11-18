package com.andy.demo.jsoup01.book.chapter;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 小说 Bean
 * Author: Andy.wang
 * Date: 2019/11/8 16:11
 */
@Data
public class Book implements Serializable {
    /**
     * 书名
     */
    private String bookName;
    /**
     * 书名索引
     */
    private String bookIndex;
    /**
     * 第一次创建时间
     */
    private Date createDate;
    /**
     * 最后一次更新时间
     */
    private Date updateDate;
    /**
     * 更新章节数量
     */
    private int chapterSize;
    /**
     * 是否更新完毕
     */
    private boolean isEnd;
}
