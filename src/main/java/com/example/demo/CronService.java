package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CronService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final String cronExpression;
    private final String cronExpressionHourly;

    public CronService(
            @Value("${cron.expression}") String cronExpression,
            @Value("${cron.expression.hourly}") String cronExpressionHourly) {
        this.cronExpression = cronExpression;
        this.cronExpressionHourly = cronExpressionHourly;
        log.info("Cron Service scheduled: {}", this.cronExpression);
        logNextFiringTime(cronExpression);
        log.info("Cron Service scheduled: {}", this.cronExpressionHourly);
        logNextFiringTime(this.cronExpressionHourly);
    }

    void logNextFiringTime(String cron) {
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);
        Date next = cronSequenceGenerator.next(new Date());
        log.info("> Next Firing Time:   {}", next);
    }

    @Scheduled(cron = "${cron.expression}")
    public void doSomething() {
        log.info("Schedule Firing({}):       {}", cronExpression, DATE_FORMAT.format(new Date()));
        logNextFiringTime(cronExpression);
    }

    @Scheduled(cron = "${cron.expression.hourly}")
    public void doSomethingElse() {
        log.info("Schedule Firing({}):       {}", cronExpressionHourly, DATE_FORMAT.format(new Date()));
        logNextFiringTime(cronExpressionHourly);
    }
}
