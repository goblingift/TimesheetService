package gift.goblin.timesheet.service;


import gift.goblin.timesheet.client.ExternalTimeTrackingClient;
import gift.goblin.timesheet.dto.ExternalTimesheetDto;
import gift.goblin.timesheet.dto.TimesheetDto;
import gift.goblin.timesheet.jpa.entity.Timesheet;
import gift.goblin.timesheet.mapper.TimesheetMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExternalTimeTrackingService {

    private final TimesheetMapper timesheetMapper;
    private final TimesheetService timesheetService;
    private final ExternalTimeTrackingClient externalTimeTrackingClient;

    public void fetchAndSaveExternalTimesheets() {
        log.info("Start fetching, mapping and saving external timesheet entries.");

        List<ExternalTimesheetDto> allTimesheets = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            YearMonth targetYearMonth = YearMonth.now().minusMonths(i);
            List<ExternalTimesheetDto> externalTimesheets = externalTimeTrackingClient.getExternalTimesheets(targetYearMonth.getMonthValue(),
                    targetYearMonth.getYear());
            log.info("Successfully fetched {} timesheets for the YearMonth: {}", externalTimesheets.size(),  targetYearMonth);
            allTimesheets.addAll(externalTimesheets);
        }

        log.info("Done with fetching timesheets- got {} timesheets from external API.", allTimesheets.size());

        int savedTimesheets = mapAndSaveExternalTimesheets(allTimesheets);
        log.info("Mapped and saved timesheets into database: {}", savedTimesheets);
    }

    private int mapAndSaveExternalTimesheets(List<ExternalTimesheetDto> externalTimesheetDtos) {
        int savedTimesheets = 0;
        for (ExternalTimesheetDto externalTimesheetDto : externalTimesheetDtos) {
            TimesheetDto timesheetDto = timesheetMapper.toDto(externalTimesheetDto);
            boolean savedTimesheet = timesheetService.saveTimesheet(timesheetDto);
            if (savedTimesheet) {
                log.debug("Successful saved external timesheet into database: {}", timesheetDto);
                savedTimesheets++;
            } else {
                log.debug("Couldn't save external timesheet into database: {}", timesheetDto);
            }
        }
        return savedTimesheets;
    }

}
