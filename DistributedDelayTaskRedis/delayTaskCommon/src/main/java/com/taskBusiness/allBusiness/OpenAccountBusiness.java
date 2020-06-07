package com.taskBusiness.allBusiness;

import com.IDelayedTask;
import com.annotation.DelayTaskType;
import com.enums.BusinessTypeEnum;
import com.taskBusiness.IDelayedTaskBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 开户延时任务业务实现
 */
@Slf4j
@Service
@DelayTaskType(topic= BusinessTypeEnum.opendAccount,topicDesc = "开户任务")
public class OpenAccountBusiness implements IDelayedTaskBusiness {

    @Override
    public boolean execute(String topic, String key, IDelayedTask delayedTask) {
      log.info("延时任务开始执行，topic：{} ，key ：{}",topic,key);
      try {
          Thread.sleep(500);
          log.info("延时任务执行成功，topic：{} ，key ：{}",topic,key);
          return true;
      }catch (Exception e){
          log.error("执行失败！！！",e);
          log.info("延时任务模块，topic：{} ,key：{}, addDelayedTask :{}, 延时任务执行失败，将要重新添加，下一次执行将会是300s以后",
                  topic,key,delayedTask.getClass().getSimpleName());
          delayedTask.addDelayedTask(topic,key,300, TimeUnit.SECONDS);
      }
      return false;
    }
}