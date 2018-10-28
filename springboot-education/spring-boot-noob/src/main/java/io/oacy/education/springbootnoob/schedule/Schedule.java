package io.oacy.education.springbootnoob.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Schedule {

//    @Scheduled(fixedRate = 2000)
    public void scheduledTask(){
        System.out.println("启动定时任务:" + new Date());
    }
}
