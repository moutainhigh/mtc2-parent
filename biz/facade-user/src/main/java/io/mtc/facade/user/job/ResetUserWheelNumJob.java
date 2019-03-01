package io.mtc.facade.user.job;

import io.mtc.common.quartz.support.JobSupport;
import io.mtc.facade.user.service.RedEnvelopeCache;
import io.mtc.facade.user.service.UserService;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 打包交易队列消费
 *
 * @author Chinhin
 * 2018/7/30
 */
public class ResetUserWheelNumJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        ApplicationContext applicationContext = JobSupport.getContext(jobExecutionContext);
        if (applicationContext == null) {
            return;
        }
        UserService userService = applicationContext.getBean(UserService.class);
        userService.resetUserWheelNum();
    }

}
