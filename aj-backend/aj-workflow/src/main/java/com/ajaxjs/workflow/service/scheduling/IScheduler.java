package com.ajaxjs.workflow.service.scheduling;

/**
 * 调度器接口，与具体的定时调度框架无关
 */
public interface IScheduler {
    String CONFIG_REPEAT = "scheduler.repeat";
    String CONFIG_USECALENDAR = "scheduler.useCalendar";
    String CONFIG_HOLIDAYS = "scheduler.holidays";
    String CONFIG_WEEKS = "scheduler.weeks";
    String CONFIG_WORKTIME = "scheduler.workTime";

    String CALENDAR_NAME = "snakerCalendar";

    String KEY = "id";
    String MODEL = "model";
    String GROUP = "snaker";

    String TYPE_EXECUTOR = "executor.";
    String TYPE_REMINDER = "reminder.";

    /**
     * 调度执行方法
     *
     * @param entity 调度 DTO
     */
    void schedule(JobEntity entity);

    /**
     * 停止调度
     *
     * @param key job 主键
     */
    void delete(String key);
}
