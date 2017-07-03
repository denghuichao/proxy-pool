package com.deng.pp.api;

import com.deng.pp.db.repositor.ProxyRepository;
import com.deng.pp.entity.ProxyEntity;
import com.deng.pp.utils.ProxyUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Created by hcdeng on 2017/6/29.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/api")
public class ProxyApi {

    @Inject
    private ProxyRepository proxyRepository;

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
    public Response getProxy() {
        ProxyEntity proxy = proxyRepository.getRandomly();
        return new Response(proxy != null , proxy!=null ? "获取成功" : "获取失败", proxy);
    }


    @GetMapping("/get_list")
    public Response getProxys(@RequestParam(value="num", defaultValue="1") int num){
       List<ProxyEntity> list = proxyRepository.getList(num);
        return new Response(true, "获取成功", list);
    }


    @GetMapping("verify")
    public Response verifyProxy(@RequestParam String proxy){
        if(proxy == null || !proxy.matches("\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+"))
            return new Response(false, "输入参数不合法",null);

        boolean useful = ProxyUtil.verifyProxy(proxy);
        if(!useful)proxyRepository.deleteByKey(proxy);
        Map<String, Object> map = new HashedMap();
        map.put("proxy", proxy);
        map.put("useful", useful);

        return new Response(true, "验证成功", map);
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
        proxyRepository.deleteByKey(proxy);
        return new Response(true, "删除成功", null);
    }
}
