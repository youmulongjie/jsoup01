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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

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

	@Test
	public void merge(){
	    String outFile = INet.DOWNLOAD_ADDR + NovelEnum.斗破苍穹.name();
	    mergeFiles(outFile+".txt", new File(outFile).listFiles());
    }

    private final void mergeFiles(String outFile, File[] files) {
        FileChannel outChannel = null;
        FileInputStream fileInputStream = null;

        FileChannel fc = null;
        ByteBuffer bb = null;
        try {
            outChannel = new FileOutputStream(outFile).getChannel();
            for (File f : files) {
                fileInputStream = new FileInputStream(f);
                fc = fileInputStream.getChannel();
                bb = ByteBuffer.allocate(1024 * 100);
                while (fc.read(bb) != -1) {
                    bb.flip();
                    outChannel.write(bb);
                    outChannel.write(ByteBuffer.wrap("\r\n\n".getBytes()));
                    bb.clear();
                }
                fc.close();
                fileInputStream.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException ignore) {
            }
        }
    }
}
