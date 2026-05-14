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
    private final ExternalTimeTrackingClient externalTimeTrackingClient;

    @Scheduled(fixedRate = 60000)
    public void fetchExternalTimesheets() {

        List<ExternalTimesheetDto> allTimesheets = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            YearMonth targetYearMonth = YearMonth.now().minusMonths(i);
            List<ExternalTimesheetDto> externalTimesheets = externalTimeTrackingClient.getExternalTimesheets(targetYearMonth.getMonthValue(),
                    targetYearMonth.getYear());
            log.info("Successfully fetched {} timesheets for the YearMonth: {}", externalTimesheets.size(),  targetYearMonth);
            allTimesheets.addAll(externalTimesheets);
        }

        log.info("Done with fetching timesheets- got {} timesheets from external API.", allTimesheets.size());

        externalTimeTrackingService.saveExternalTimesheet(allTimesheets);
    }

}
