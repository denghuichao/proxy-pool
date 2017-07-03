package com.deng.pp.boot;

import com.deng.pp.schedule.FetchScheduler;
import com.deng.pp.schedule.Scheduler;
import com.deng.pp.schedule.VerifyScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hcdeng on 2017/6/30.
 */
@SpringBootApplication(scanBasePackages={"com.deng.pp.api"})
public class ApplicationBoot {
    private static final List<Scheduler> schedules = Arrays.asList(
            new FetchScheduler(30, TimeUnit.MINUTES),
            new VerifyScheduler(10, TimeUnit.MINUTES));

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBoot.class, args);

        //for(Scheduler schedule : schedules)
        //    schedule.schedule();
    }
}
