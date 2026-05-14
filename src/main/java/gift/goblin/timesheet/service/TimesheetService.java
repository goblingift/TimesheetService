package gift.goblin.timesheet.service;

import gift.goblin.timesheet.dto.TimesheetDto;
import gift.goblin.timesheet.jpa.entity.Timesheet;
import gift.goblin.timesheet.jpa.repo.TimesheetRepository;
import gift.goblin.timesheet.mapper.TimesheetMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveTimesheet(TimesheetDto timesheetDto) {

        log.info("Called saveTimesheet with dto : {}", timesheetDto);

        List<Timesheet> existingEntries = timesheetRepository.getTimesheetByEmployeeIdAndWorkDate(timesheetDto.getEmployeeId(), timesheetDto.getWorkDate());
        if (!existingEntries.isEmpty()) {
            log.warn("Found existing entries for your new timesheet entry: {}", (long) existingEntries.size());
            Timesheet existingEntry = existingEntries.getFirst();
            return replaceExistingTimesheet(existingEntry, timesheetDto);
        } else {
            Timesheet entity = timesheetMapper.toEntity(timesheetDto);
            Timesheet savedEntity = timesheetRepository.save(entity);
            log.info("Saved new timesheet entry: {}", savedEntity);
            return true;
        }

    }

    private boolean replaceExistingTimesheet(Timesheet existingEntry, TimesheetDto newTimesheetDto) {

        if (existingEntry.getRecordedAt().isBefore(newTimesheetDto.getRecordedAt())) {
            log.info("The existing timesheet {} was recorded before the new timesheet entry {} - replace with new data!", existingEntry, newTimesheetDto);
            timesheetRepository.delete(existingEntry);
            timesheetRepository.flush();
            Timesheet newEntity = timesheetMapper.toEntity(newTimesheetDto);
            timesheetRepository.save(newEntity);
            return true;
        } else {
            log.debug("Dont replace existing {} timesheet entry", existingEntry);
            return false;
        }

    }



}
