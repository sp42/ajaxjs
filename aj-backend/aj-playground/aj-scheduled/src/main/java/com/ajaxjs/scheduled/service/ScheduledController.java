package com.ajaxjs.scheduled.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.scheduled.model.JobInfoVO;
import com.ajaxjs.scheduled.model.ScheduledParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/scheduled")
public class ScheduledController {
    @Autowired
    private ScheduleHandler scheduleHandler;

    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

    static final String SQL = "SELECT `id`, \n" +
            "       `job_name` AS jobName, \n" +
            "       `job_express` AS jobExpress, \n" +
            "       `job_app_name` AS jobAppName, \n" +
            "       `job_class_name` AS jobClassName, \n" +
            "       `job_method_name` AS jobMethodName, \n" +
            "       `job_status` AS jobStatus \n" +
            "FROM `schedule_job_info`";

    @GetMapping
    public PageResult<JobInfoVO> list(@RequestParam(required = false) String name) {
        String sql = SQL;

        if (StringUtils.hasText(name))
            sql += " WHERE job_name LIKE '%" + name + "%'";

        return CRUD.page(JobInfoVO.class, sql, null);
    }

    @PostMapping("/pause")
    public boolean pause(@RequestBody ScheduledParam scheduledParam) {
        scheduleHandler.cancel(scheduledParam.getJobExpress(), scheduledParam.getJobClassName(), scheduledParam.getId(), true);

        return true;
    }

    @PostMapping("/resume")
    public boolean resume(@RequestBody ScheduledParam scheduledParam) {
        scheduleHandler.resume(
                scheduledParam.getJobClassName(),
                scheduledParam.getJobMethodName(),
                scheduledParam.getJobExpress(),
                scheduledParam.getId());

        return true;
    }

    @DeleteMapping("/remove")
    public boolean remove(@RequestBody ScheduledParam scheduledParam) {
        scheduleHandler.remove(scheduledParam.getJobExpress(), scheduledParam.getJobClassName(), scheduledParam.getId());

        return true;
    }

    @PostMapping("/trigger")
    public boolean trigger(@RequestBody ScheduledParam scheduledParam) {
        scheduleHandler.trigger(scheduledParam.getJobClassName(), scheduledParam.getJobMethodName());

        return true;
    }
}
