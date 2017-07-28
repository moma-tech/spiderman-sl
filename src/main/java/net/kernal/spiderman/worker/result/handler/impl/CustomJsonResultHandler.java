package net.kernal.spiderman.worker.result.handler.impl;

import com.alibaba.fastjson.JSON;
import net.kernal.spiderman.kit.Context;
import net.kernal.spiderman.kit.Counter;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.worker.extract.ExtractResult;
import net.kernal.spiderman.worker.result.ResultTask;
import net.kernal.spiderman.worker.result.handler.ResultHandler;

import java.io.File;
import java.io.IOException;

/**
 * json result handler
 * Created by ivan on 17-7-27.
 **/
public class CustomJsonResultHandler implements ResultHandler {

    public void handle(ResultTask task, Counter c) {
        final ExtractResult er = task.getResult();
        final String url = task.getRequest().getUrl();
        final String json = JSON.toJSONString(er.getFields(), true);
        String lineSeparator = System.getProperty("line.separator");
        String links = er.getFields().getString("链接")+lineSeparator;
        final Context ctx = context.get();
        final String fileName = String.format("/json_result_%s_%s.json", er.getPageName(),
                er.getModelName());
        final String path = ctx.getParams().getString("worker.result.store", "store/result") + fileName;
        File file = new File(path);
        try {
            K.appendFile(file, links);
            final String fmt = "\r\n保存第%s个[page=%s, model=%s, url=%s]结果[%s]";
            System.err.println(String.format(fmt, c, er.getPageName(), er.getModelName(), url, file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
