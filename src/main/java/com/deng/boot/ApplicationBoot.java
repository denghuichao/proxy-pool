package com.deng.boot;

import com.deng.schedule.FetchScheduler;
import com.deng.schedule.Scheduler;
import com.deng.schedule.ValidateScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hcdeng on 2017/6/30.
 */
@SpringBootApplication(scanBasePackages={"com.deng.api"})
public class ApplicationBoot {
    private static final List<Scheduler> schedules = Arrays.asList(
            new FetchScheduler(30, TimeUnit.MINUTES),
            new ValidateScheduler(10, TimeUnit.MINUTES));

    public static void main(String[] args) {

        //for(Scheduler schedule : schedules)
         //   schedule.schedule();

        SpringApplication.run(ApplicationBoot.class, args);
    }
}
