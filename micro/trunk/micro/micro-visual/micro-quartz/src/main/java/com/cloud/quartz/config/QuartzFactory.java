
package com.cloud.quartz.config;

import com.cloud.quartz.constant.enums.QuartzEnum;
import com.cloud.quartz.entity.SysJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wengshij
 * @date Created in 2019/6/25 9:51
 * @description: 动态任务工厂
 * @modified By:wengshij
 */
@Slf4j
@DisallowConcurrentExecution
public class QuartzFactory implements Job {

	@Autowired
	private QuartzInvokeFactory quartzInvokeFactory;


	@Override
	@SneakyThrows
	public void execute(JobExecutionContext jobExecutionContext) {
		SysJob sysJob = (SysJob) jobExecutionContext.getMergedJobDataMap().get(QuartzEnum.SCHEDULE_JOB_KEY.getType());
		quartzInvokeFactory.init(sysJob, jobExecutionContext.getTrigger());
	}
}
