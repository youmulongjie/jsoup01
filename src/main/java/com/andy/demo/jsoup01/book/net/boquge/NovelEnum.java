package com.andy.demo.jsoup01.book.net.boquge;

/**
 * （笔趣阁 网站）小说、标号
 */
public enum NovelEnum {
    斗破苍穹(46773);
    // add what you like read ...
    // such as 斗破苍穹(46773)

    private Integer number;

    NovelEnum(Integer number) {
        this.number = number;
    }

    /**
     * @return the number
     */
    public final Integer getNumber() {
        return number;
    }
}
