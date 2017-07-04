package com.deng.pp.fetcher;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hcdeng on 2017/6/29.
 */
public abstract class AbstractFetcher<T> implements Fetcher {

    private static final String[][] HEADERS = new String[][]{
            {"Connection", "keep-alive"},
            {"Cache-Control", "max-age=0"},
            {"Upgrade-Insecure-Requests", "1"},
            {"User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko)"},
            {"Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"},
            {"Accept-Encoding", "gzip, deflate, sdch"},
            {"Accept-Language", "zh-CN,zh;q=0.8"},
    };

    private static final Logger logger = LoggerFactory.getLogger(AbstractFetcher.class);

    protected int pageIndex = 1;

    private int totalPage = 10;
    private long interval = 1000;

    public AbstractFetcher(int totalPage, long interval) {
        this.totalPage = totalPage;
        this.interval = interval;
    }

    public boolean hasNextPage() {
        return pageIndex <= totalPage;
    }

    public String nextPage() {
        String html = "";
        String url = pageUrl();
        pageIndex++;
        logger.info("fetching page: " + url);
        try {
            Connection connection = Jsoup.connect(url);
            for (String[] head : HEADERS) {
                connection.header(head[0], head[1]);
            }
            connection.timeout(4000).followRedirects(true);
            html = connection.execute().parse().html();//执行
        } catch (IOException e) {
            logger.info("fetch page error: " + e.getMessage());
        }

        return html;
    }


    public List<T> fetchAll() {
        List<T> res = new ArrayList<>();
        while (hasNextPage()) {
            String html = nextPage();
            res.add(parseHtml(html));
            try {
                Thread.sleep(interval);
            }catch (InterruptedException e){}
        }

        return res;
    }

    public void fetchAll(Consumer<T> comsumer) {
        while (hasNextPage()) {
            String html = nextPage();
            comsumer.consume(parseHtml(html));
            try {
                Thread.sleep(interval);
            }catch (InterruptedException e){}
        }
    }


    public int getPageIndex() {return pageIndex;}


    public int getTotalPage() {return totalPage;}

    public long getInterval() {return interval;}

    protected abstract String pageUrl();

    protected abstract T parseHtml(String html);

    public  interface Consumer<T>{
        void consume(T t);
    }
}
