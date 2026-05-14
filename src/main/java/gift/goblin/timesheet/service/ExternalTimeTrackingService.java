package gift.goblin.timesheet.service;


import gift.goblin.timesheet.dto.ExternalTimesheetDto;
import gift.goblin.timesheet.dto.TimesheetDto;
import gift.goblin.timesheet.jpa.entity.Timesheet;
import gift.goblin.timesheet.mapper.TimesheetMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExternalTimeTrackingService {

    private final TimesheetMapper timesheetMapper;
    private final TimesheetService timesheetService;

    public void saveExternalTimesheet(List<ExternalTimesheetDto> externalTimesheetDtos) {
        log.info("Start mapping and saving {} external timesheet dtos into the database.",  externalTimesheetDtos.size());

        for (ExternalTimesheetDto externalTimesheetDto : externalTimesheetDtos) {
            TimesheetDto timesheetDto = timesheetMapper.toDto(externalTimesheetDto);
            timesheetService.saveTimesheet(timesheetDto);
        }

    }

}
