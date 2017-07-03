package com.deng.fetcher;

import com.deng.entity.ProxyEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hcdeng on 2017/6/29.
 * http://www.kuaidaili.com 代理爬取
 */
public class KuaiDailiFetcher extends AbstractFetcher<List<ProxyEntity>> {

    private static final Logger logger = LoggerFactory.getLogger(KuaiDailiFetcher.class);

    private static final String BASE_URL = "http://www.kuaidaili.com/free/";

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public KuaiDailiFetcher() {
        this(10);
    }

    public KuaiDailiFetcher(int totalPage) {
        this(totalPage, 1000);
    }

    public KuaiDailiFetcher(int totalPage, long interval) {
        super(totalPage, interval);
    }

    @Override
    protected String pageUrl() {
        if(pageIndex == 1)return BASE_URL;
        else return BASE_URL + "inha/"+pageIndex+"/";
    }

    @Override
    protected List<ProxyEntity> parseHtml(String html) {
        List<ProxyEntity> res = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements tables = doc.select("tbody");

        for(Element table : tables){
            Elements trs = table.select("tr");

            for(int i = 0; i < trs.size(); i++){
                Element tr = trs.get(i);
                Elements tds = tr.select("td");
                if(tds.size() != 7)continue;

                /**
                 * <td data-title="IP">58.214.135.63</td>
                 * <td data-title="PORT">808</td>
                 * <td data-title="匿名度">高匿名</td>
                 * <td data-title="类型">HTTP</td>
                 * <td data-title="位置">中国 江苏省 无锡市 电信</td>
                 * <td data-title="响应速度">1秒</td>
                 * <td data-title="最后验证时间">2017-06-24 15:37:07</td>
                 */

                ProxyEntity enity = new ProxyEntity();
                enity.setIp(tds.get(0).text().trim());
                enity.setPort(Integer.parseInt(tds.get(1).text()));
                enity.setAgentType(tds.get(2).text().trim());
                enity.setLocation(tds.get(4).text().trim());

                Date date = new Date();
                try {
                    date = SDF.parse(tds.get(4).text().trim());
                }catch (ParseException e){}

                enity.setLastValidateTime(date);

                logger.info("got an agent: "+enity);
                res.add(enity);
            }
        }
        return res;
    }
}
