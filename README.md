Jsoup demo
================================
Springboot 版本：2.0.5.RELEASE<br>
<br>
以 笔趣阁（ https://www.boquge.com/ ）为例，下载小说。测试中以“斗破苍穹”为例，30秒内下载完整部小说，并合并成一个 txt 文件，方便直接加入到你的 小说阅读器中。

## 执行测试 
```java
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Jsoup01ApplicationTests {
    private String bookName = "斗破苍穹";

    @Test
    public void download() {
        long begin = System.currentTimeMillis();
        INet boquge = new Boquge(bookName);

        DownloadMain boqugeMain = new DownloadMain(boquge);
        boqugeMain.download();
        long end = System.currentTimeMillis();

        log.info("耗时：" + (end - begin) / 1000 + "s");
    }

}
```
## Andy.wang

<img src="doc/594580820.jpg" width="15%" alt="Andy.wang的QQ"/>