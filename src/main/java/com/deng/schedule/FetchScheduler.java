package com.deng.schedule;

import com.deng.entity.RawProxy;
import com.deng.fetcher.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hcdeng on 2017/6/29.
 */
public class FetchScheduler extends Scheduler {

    public FetchScheduler(long defaultInterval, TimeUnit defaultUnit) {
        super(defaultInterval, defaultUnit);
    }

    @Override
    public void run() {
        List<AbstractFetcher<List<RawProxy>>> fetchers =
                Arrays.asList(new GoubanjiaFetcher(10),
                        new KuaiDailiFetcher(10),
                        new Www66IPFetcher(10),
                        new XichiDailiFetcher(10));

        for (AbstractFetcher<List<RawProxy>> fetcher : fetchers) {
            List<List<RawProxy>> lists = fetcher.fetchAll();
            //todo saving the proxys
        }
    }
}
