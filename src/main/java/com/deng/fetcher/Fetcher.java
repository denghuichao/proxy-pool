package com.deng.fetcher;

/**
 * Created by hcdeng on 2017/6/29.
 */
public interface Fetcher {

    boolean hasNextPage();

    String nextPage();
}
