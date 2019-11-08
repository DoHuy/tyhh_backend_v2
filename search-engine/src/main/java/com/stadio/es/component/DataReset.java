package com.stadio.es.component;

import com.stadio.es.service.IExamService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataReset {
    private Logger logger = LogManager.getLogger(DataReset.class);

    @Autowired
    IExamService examService;

    @Scheduled(cron = "${schedule.daily}")
    public void resetExamData(){
        examService.resetData();
    }
}
