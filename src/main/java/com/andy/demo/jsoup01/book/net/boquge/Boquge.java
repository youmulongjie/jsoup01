package com.andy.demo.jsoup01.book.net.boquge;

import com.andy.demo.jsoup01.book.INet;
import com.andy.demo.jsoup01.book.chapter.Chapter;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 笔趣阁 网站（https://www.boquge.com/）
 */
public class Boquge implements INet {
    // 笔趣阁URL
    private final static String URL = "https://www.boquge.com/";
    // 小说URL 前缀
    private final static String PREFIX = "book";
    private NovelEnum novelEnum;

    public Boquge(NovelEnum novelEnum) {
        this.novelEnum = novelEnum;
    }

    /**
     * 小说名称
     *
     * @return
     */
    @Override
    public String bookName() {
        return novelEnum.name();
    }

    /**
     * 小说 更新的所有章节集合
     *
     * @return
     * @throws IOException
     */
    @Override
    public List<Chapter> getBooks() throws IOException {
        Document document = Jsoup
                .connect(URL + PREFIX + "/" + this.novelEnum.getNumber() + "/")
                .timeout(10000).get();
        Elements chapterListElements = document.select("ul#chapters-list");
        Validate.notNull(chapterListElements, "没有找到 id=[chapters-list]的ul元素.");

        Elements liElements = chapterListElements.get(0).getElementsByTag("li");
        List<Chapter> list  = new ArrayList<>();

        Stream.iterate(0, i -> i + 1).limit(liElements.size()).forEach((i -> {
            Elements aElements = liElements.get(i).getElementsByTag("a");
            Validate.notNull(aElements, "没有找到 a标签的元素.");

            Element aElement = null;
            if (null != aElements && aElements.size() > 0) {
                aElement = aElements.get(0);

                Chapter book = new Chapter(i, aElement.attr("href"),
                        aElement.text());
                list.add(book);
            }
        }));
        return list;
    }

    /**
     * 小说章节 内容
     *
     * @param book
     * @throws IOException
     */
    @Override
    public void getContext(Chapter book) throws IOException {
        Document document = Jsoup.connect(URL + book.getUrl()).timeout(10000)
                .get();
        Elements txtContentElements = document.select("div#txtContent");
        Validate.notNull(txtContentElements, "没有找到 id=[txtContent]的div元素.");

        Element txtContentElement = txtContentElements.get(0);

        book.setContext(book.getTitle() + "\r\n" + txtContentElement.text());
    }
}
