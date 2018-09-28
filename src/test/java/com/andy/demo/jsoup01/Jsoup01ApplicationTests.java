package com.andy.demo.jsoup01;

import com.andy.demo.jsoup01.book.DownloadMain;
import com.andy.demo.jsoup01.book.INet;
import com.andy.demo.jsoup01.book.net.boquge.Boquge;
import com.andy.demo.jsoup01.book.net.boquge.NovelEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Jsoup01ApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void download() {
		long begin = System.currentTimeMillis();
		INet boquge = new Boquge(NovelEnum.斗破苍穹);
		DownloadMain boqugeMain = new DownloadMain(boquge);
		boqugeMain.download();
		long end = System.currentTimeMillis();

		log.info("耗时：" + (end - begin) / 1000 + "s");
	}

}
