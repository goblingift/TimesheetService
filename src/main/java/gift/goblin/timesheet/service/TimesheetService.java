package gift.goblin.timesheet.service;

import gift.goblin.timesheet.dto.TimesheetDto;
import gift.goblin.timesheet.jpa.entity.Timesheet;
import gift.goblin.timesheet.jpa.repo.TimesheetRepository;
import gift.goblin.timesheet.mapper.TimesheetMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final TimesheetMapper timesheetMapper;

    /**
     * Saves the new timesheet with the given informations of the dto. If there´s already an entry for the employee on the
     * same day, it will decide based on the recordedAt timestamp and takes the values from the newer entry.
     * @param timesheetDto dto with all required informations for the new timesheet entry.
     * @return the saved entity with all its fields.
     */
    public TimesheetDto saveTimesheet(TimesheetDto timesheetDto) {
        List<Timesheet> existingEntries = timesheetRepository.getTimesheetByEmployeeIdAndWorkDate(timesheetDto.getEmployeeId(), timesheetDto.getWorkDate());
        if (!existingEntries.isEmpty()) {
            log.warn("Found existing entries for your new timesheet entry: {}", (long) existingEntries.size());
            Timesheet existingEntry = existingEntries.getFirst();
            TimesheetDto mappedExistingEntry = timesheetMapper.toDto(existingEntry);
            log.warn("Existing timesheet entry: {}", mappedExistingEntry);
            return mappedExistingEntry;
        } else {
            Timesheet entity = timesheetMapper.toEntity(timesheetDto);
            Timesheet savedEntity = timesheetRepository.save(entity);
            log.info("Saved new timesheet entry: {}", savedEntity);
            return timesheetMapper.toDto(savedEntity);
        }

    }


}
