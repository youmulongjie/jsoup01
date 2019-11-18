package com.andy.demo.jsoup01.book.net.boquge;

import com.andy.demo.jsoup01.book.INet;
import com.andy.demo.jsoup01.book.chapter.Chapter;
import com.andy.demo.jsoup01.book.exception.BookNotFoundException;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * 笔趣阁 网站（https://www.boquge.com/）
 */
@Slf4j
public class Boquge implements INet {
    // 笔趣阁网站 URL
    private final static String WEBSITE_URL = "https://www.boquge.com/";

    // 小说URL下载 前缀
    private final static String DOWNLOAD_PREFIX = "book/";

    // 超时时间
    private final static int TIMEOUT = 10000;

    /**
     * 小说名称
     */
    private String bookName;

    public Boquge(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public boolean isFound() {
        return !bookIndex().equals(BOOK_NOT_FOUND);
    }

    /**
     * 小说名称
     *
     * @return 小说名称
     */
    @Override
    public String bookName() {
        return this.bookName;
    }

    /**
     * 获取 小说索引
     *
     * @return 返货小说在网站的索引编号，-1则说明没有搜索到该小说
     */
    @Override
    public String bookIndex() {
        log.info("获取小说[{}]在网站的编号 start.", bookName);
        AtomicReference<String> bookIndex = new AtomicReference<>(BOOK_NOT_FOUND);

        String search = "search.htm?keyword=";
        String searchUrl = WEBSITE_URL + search + bookName;
        log.info("搜索的URL：{}", searchUrl);
        try {
            Document document = Jsoup.connect(searchUrl).timeout(TIMEOUT).get();
            Elements searchListElements = document.select("li.list-group-item");

            searchListElements.stream().map(
                    element -> element.getElementsContainingText(bookName)
            ).forEach(elements -> elements.stream().filter(
                    e -> e.text().equals(bookName))
                    .forEach(e -> {
                        String href = e.attr("href");
                        String num = StringUtil.cutSurrounding(href, "/xs/", "/index.html");
                        if (!StringUtils.isEmpty(num)) {
                            log.info("万幸找到了，信息如下：href={}, 编号={}", href, num);
                            bookIndex.set(num);

                        }
                    }));
        } catch (IOException e) {
            log.error("获取小说编号失败：{}", e.getMessage());
        }

        log.info("编号为{}", bookIndex.get());
        return bookIndex.get();
    }

    /**
     * 小说 更新的所有章节集合
     *
     * @return 小说的章节 集合
     */
    @Override
    public List<Chapter> chapterList() throws Exception {
        String bookIndex = this.bookIndex();
        if (bookIndex.equals(BOOK_NOT_FOUND)) {
            throw new BookNotFoundException("抱歉没有检索到【" + bookName + "】，任务终止.");
        }

        Document document = Jsoup
                .connect(WEBSITE_URL + DOWNLOAD_PREFIX + "/" + bookIndex + "/")
                .timeout(TIMEOUT).get();

        Elements chapterListElements = document.select("ul#chapters-list");
        Validate.notNull(chapterListElements, "没有找到 id=[chapters-list]的ul元素.");

        Elements liElements = chapterListElements.get(0).getElementsByTag("li");
        List<Chapter> list = new ArrayList<>();

        Stream.iterate(0, i -> i + 1).limit(liElements.size()).forEach(i -> {
            Elements aElements = liElements.get(i).getElementsByTag("a");
            Validate.notNull(aElements, "没有找到 a标签的元素.");

            Element aElement;
            if (aElements.size() > 0) {
                aElement = aElements.get(0);

                Chapter book = new Chapter(i, aElement.attr("href"),
                        aElement.text());
                list.add(book);
            }
        });
        return list;
    }

    /**
     * 小说章节 内容
     *
     * @param chapter
     */
    @Override
    public void getContext(Chapter chapter) throws IOException {
        Document document = Jsoup.connect(WEBSITE_URL + chapter.getUrl()).timeout(TIMEOUT)
                .get();
        Elements txtContentElements = document.select("div#txtContent");
        Validate.notNull(txtContentElements, "没有找到 id=[txtContent]的div元素.");

        Element txtContentElement = txtContentElements.get(0);
        chapter.setContext(chapter.getTitle() + "\r\n" + txtContentElement.text());
    }
}
