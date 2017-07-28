package net.kernal.spiderman.worker.result.handler.impl;

import net.kernal.spiderman.kit.Context;
import net.kernal.spiderman.kit.Counter;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.worker.extract.ExtractResult;
import net.kernal.spiderman.worker.result.ResultTask;
import net.kernal.spiderman.worker.result.handler.ResultHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

/**
 * Handle Html Body Part
 * Created by ivan on 17-7-27.
 **/
public class CustomBodyResultHandler implements ResultHandler {
    @Override
    /**
     * <span id="ContentPlaceHolder1_lbAuthor">发布人:中华人民共和国国务院</span>
     *<span id="ContentPlaceHolder1_ViewFrom">文章来源:中国水利工程协会</span>
     *<span id="ContentPlaceHolder1_Time">发布时间:2010 年 6 月 22 日</span>
     */
    public void handle(ResultTask task, Counter c) {
        final ExtractResult er = task.getResult();
        final String content = er.getResponseBody();
        String lineSeparator = System.getProperty("line.separator");
        Document doc = Jsoup.parse(content);
        Element span = doc.getElementById("ContentPlaceHolder1_lbCONTENT");
        Element author = doc.getElementById("ContentPlaceHolder1_lbAuthor");
        Element from = doc.getElementById("ContentPlaceHolder1_ViewFrom");
        Element time = doc.getElementById("ContentPlaceHolder1_Time");
        String html = author.text() + lineSeparator + from.text() + lineSeparator + time.text() + lineSeparator + span.html();
        Element title = doc.getElementById("ContentPlaceHolder1_DIVTITLE");
        String fileName = c.get()+"-"+title.text();
        final Context ctx = context.get();
        final String path = ctx.getParams().getString("worker.result.store", "store/result") + "/" + fileName;
        File file = new File(path);
        try {
            K.writeFile(file, html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
