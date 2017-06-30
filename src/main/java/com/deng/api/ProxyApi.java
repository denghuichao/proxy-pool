package com.deng.api;

import com.deng.entity.RawProxy;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hcdeng on 2017/6/29.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/api")
public class ProxyApi {
    private static final String API_LIST =
            "get: get an usable proxy\n"+
            "refresh: refresh proxy pool\n"+
            "get_all: get all proxy from proxy \n"+
            "delete?proxy=127.0.0.1:8080': delete an unable proxy\n";


    @GetMapping("/")
    public Response index(){
        return new Response(true, "", API_LIST);
    }

    @GetMapping("/get")
    public Response<RawProxy> getProxy() {
        return new Response<>(true, "获取成功", new RawProxy());
    }


    @GetMapping("/get_all")
    public Response<List<RawProxy>> getProxys(@RequestParam(value="num", defaultValue="1") int num){
        return new Response<>(true, "获取成功", new ArrayList<>());
    }

    @GetMapping("/query")
    public Response<List<RawProxy>> queryProxys(@RequestParam String params){
        //todo query proxys by the query params
        System.out.println(params);
        return new Response<>(true, "查询成功", new ArrayList<>());
    }

    @GetMapping("/refresh")
    public Response refreshProxys(){
        return new Response(true, "刷新请求已受理",null);
    }

    @GetMapping("/delete")
    public Response deleteProxy(@RequestParam String proxy){
        if(proxy == null || !proxy.matches("\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+"))
            return new Response(false, "输入参数不合法",null);

        //todo delete a unuseful proxy...
        return new Response(true, "删除成功", null);
    }
}
