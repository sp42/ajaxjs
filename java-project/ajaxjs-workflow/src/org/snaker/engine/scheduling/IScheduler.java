package org.snaker.engine.scheduling;

/**
 * 调度器接口，与具体的定时调度框架无关
 * 
 * @author yuqs
 * @since 1.4
 */
public interface IScheduler {
	public static final String CONFIG_REPEAT = "scheduler.repeat";
	public static final String CONFIG_USECALENDAR = "scheduler.useCalendar";
	public static final String CONFIG_HOLIDAYS = "scheduler.holidays";
	public static final String CONFIG_WEEKS = "scheduler.weeks";
	public static final String CONFIG_WORKTIME = "scheduler.workTime";

	public static final String CALENDAR_NAME = "snakerCalendar";

	public static final String KEY = "id";
	public static final String MODEL = "model";
	public static final String GROUP = "snaker";

	public static final String TYPE_EXECUTOR = "executor.";
	public static final String TYPE_REMINDER = "reminder.";

	/**
	 * 调度执行方法
	 * 
	 * @param entity
	 *            调度DTO
	 */
	void schedule(JobEntity entity);

	/**
	 * 停止调度
	 * 
	 * @param key
	 *            job主键
	 */
	void delete(String key);
}
