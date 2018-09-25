package com.andy.demo.jsoup01.book.net.boquge;

/**
 * （笔趣阁 网站）小说、标号
 */
public enum NovelEnum {
    斗破苍穹(46773), 秋夜传(91821);
    // add what you like read ...

    private Integer number;

    private NovelEnum(Integer number) {
        this.number = number;
    }

    /**
     * @return the number
     */
    public final Integer getNumber() {
        return number;
    }
}
