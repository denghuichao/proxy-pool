package com.deng.fetcher;

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
 * http://www.xicidaili.com 代理爬取
 */
public class XichiDailiFetcher extends AbstractFetcher<List<ProxyEntity>> {

    private static final String BASE_URL = "http://www.xicidaili.com/";

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yy-MM-dd HH:mm");

    private static final Logger logger = LoggerFactory.getLogger(XichiDailiFetcher.class);


    public XichiDailiFetcher() {
        this(10, 1000);
    }

    public XichiDailiFetcher(int totalPage) {
        this(totalPage, 1000);
    }

    public XichiDailiFetcher(int totalPage, long interval) {
        super(totalPage, interval);
    }

    @Override
    protected String pageUrl() {
        if (pageIndex <= totalPage / 2)
            return BASE_URL + "nn/" + pageIndex;
        else
            return BASE_URL + "nt/" + (pageIndex - (totalPage / 2));

    }

    @Override
    protected List<ProxyEntity> parseHtml(String html) {

        List<ProxyEntity> res = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements tables = doc.getElementById("ip_list").select("tbody");

        for (Element table : tables) {
            Elements trs = table.select("tr");

            for (int i = 1; i < trs.size(); i++) {
                Element tr = trs.get(i);
                Elements tds = tr.select("td");
                if (tds.size() != 10) continue;

                /**
                 *  <tr class="odd">
                 *  <td class="country"><img src="http://fs.xicidaili.com/images/flag/cn.png" alt="Cn" /></td>
                 *  <td>183.151.13.16</td>
                 *  <td>80</td>
                 *  <td> <a href="/2017-06-29/zhejiang">浙江丽水</a> </td>
                 *  <td class="country">高匿</td>
                 *  <td>HTTPS</td>
                 *  <td class="country">
                 *      <div title="7.174秒" class="bar">
                 *      <div class="bar_inner slow" style="width:65%">
                 *      </div>
                 *      </div> </td>
                 *  <td class="country">
                 *      <div title="1.434秒" class="bar">
                 *      <div class="bar_inner medium" style="width:83%">
                 *      </div>
                 *      </div></td>
                 *  <td>14小时</td>
                 *  <td>17-06-29 15:26</td>
                 *  </tr>
                 */

                ProxyEntity enity = new ProxyEntity();
                enity.setIp(tds.get(1).text().trim());
                enity.setPort(Integer.parseInt(tds.get(2).text().trim()));
                enity.setAgentType(tds.get(4).text().trim());
                enity.setLocation(tds.get(3).text().trim());

                Date date = new Date();
                try {
                    date = SDF.parse(tds.get(9).text().trim());
                } catch (ParseException e) {
                }

                enity.setLastValidateTime(date);

                logger.info("got an agent: " + enity);
                res.add(enity);
            }
        }
        return res;
    }
}
