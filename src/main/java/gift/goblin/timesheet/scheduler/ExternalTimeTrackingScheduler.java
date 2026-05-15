package gift.goblin.timesheet.scheduler;

import gift.goblin.timesheet.client.ExternalTimeTrackingClient;
import gift.goblin.timesheet.dto.ExternalTimesheetDto;
import gift.goblin.timesheet.jpa.entity.Timesheet;
import gift.goblin.timesheet.service.ExternalTimeTrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalTimeTrackingScheduler {

    private final ExternalTimeTrackingService externalTimeTrackingService;

    @Scheduled(fixedRate = 60000)
    public void fetchExternalTimesheets() {
        log.info("TS-1.1: Triggered ExternalTimeTrackingScheduler- Fetching timesheets from external API and save into DB.");
        externalTimeTrackingService.fetchAndSaveExternalTimesheets();
        log.info("TS-1.2: Done with fetching and storing timesheets.");
    }

}
