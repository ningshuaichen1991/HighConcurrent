package com.gateWay;

import com.annotation.DelayTaskType;
import com.taskBusiness.IDelayedTaskBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 根据topic路由具体的业务监听类
 */
@Component
@Slf4j
public class DelayTaskBusinessGateWay {

    @Autowired
    private List<IDelayedTaskBusiness> delayedTaskBusinessList;


    public IDelayedTaskBusiness route(String topic){
        for(IDelayedTaskBusiness service : delayedTaskBusinessList){
            DelayTaskType delayTaskType = service.getClass().getAnnotation(DelayTaskType.class);
            if(delayTaskType.topic().getBusinessValue().equals(topic)){
                log.info("已导航到具体的延时任务业务类，{}",service.getClass().getSimpleName());
                return service;
            }
        }
       throw new RuntimeException("topic出错~~~");
    }
}
