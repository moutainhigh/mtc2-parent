package io.mtc.service.currency.config;

import io.mtc.common.mq.aliyun.Constants;
import io.mtc.common.mq.aliyun.Consumer;
import io.mtc.common.mq.aliyun.Producer;
import io.mtc.service.currency.service.CreateCurrencyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 消息队列
 *
 * @author Chinhin
 * 2018/7/28
 */
@Configuration
public class MqConfig {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private CreateCurrencyService createCurrencyService;

    @Bean
    public Producer ethTransPendingProducer() {
        Constants.setEnv(applicationContext.getEnvironment().getActiveProfiles()[0]);
        return new Producer(Constants.Tag.ETH_BIZ_TRANS_PENDING);
    }

    @Bean
    public Consumer ethTransCompleteConsumer() {
        Constants.setEnv(applicationContext.getEnvironment().getActiveProfiles()[0]);
        return new Consumer(Constants.Topic.MTC_BIZ_TRANS,
                Constants.Tag.ETH_BIZ_CREATION_COMPLETE,
                createCurrencyService);
    }

}