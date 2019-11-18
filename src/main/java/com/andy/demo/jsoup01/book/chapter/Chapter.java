package com.andy.demo.jsoup01.book.chapter;

import lombok.Data;

/**
 * 章节 Bean
 */
@Data
public class Chapter {
    // 下载章节索引
    private Integer index;
    // 章节URL地址
    private String url;
    // 章节 标题
    private String title;

    /**
     * 每章内容
     */
    private String context;

    public Chapter(Integer index, String url, String title) {
        this.index = index;
        this.url = url;
        this.title = title;
    }
}
