package pl.coddlers.scheduled_tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GitScheduledTasks {

    @Scheduled(cron = "0 50 23 * * 1-7", zone = "CET")
    public void lazyRepositoryForking() {
        log.info("Executed lazy forking task");
    }
}
