package gift.goblin.timesheet.service;

import gift.goblin.timesheet.dto.TimesheetDto;
import gift.goblin.timesheet.exception.TimesheetConcurrencyException;
import gift.goblin.timesheet.jpa.entity.Timesheet;
import gift.goblin.timesheet.jpa.repo.TimesheetRepository;
import gift.goblin.timesheet.mapper.TimesheetMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

    @Value("${app.simulate-slow-database:false}")
    private boolean simulateDatabaseDelay;

    /**
     * Saves the new timesheet with the given informations of the dto. If there´s already an entry for the employee on the
     * same day, it will decide based on the recordedAt timestamp and takes the values from the newer entry.
     * @param timesheetDto dto with all required informations for the new timesheet entry.
     * @return true if the timesheet data was written successful into database, false if otherwise (like newer entry already existed).
     * @throws TimesheetConcurrencyException if two or more processes try to update the same timesheet-entity, this exception could get thrown.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveTimesheet(TimesheetDto timesheetDto) throws TimesheetConcurrencyException {

        log.info("Called saveTimesheet with dto : {}", timesheetDto);

        List<Timesheet> existingEntries = timesheetRepository.getTimesheetByEmployeeIdAndWorkDate(timesheetDto.getEmployeeId(), timesheetDto.getWorkDate());
        if (!existingEntries.isEmpty()) {
            log.warn("Found existing entries for your new timesheet entry: {}", (long) existingEntries.size());
            Timesheet existingEntry = existingEntries.getFirst();
            return replaceExistingTimesheet(existingEntry, timesheetDto);
        } else {
            Timesheet entity = timesheetMapper.toEntity(timesheetDto);
            simulateSlowDatabase(simulateDatabaseDelay);
            Timesheet savedEntity = timesheetRepository.saveAndFlush(entity);
            log.info("Saved new timesheet entry: {}", savedEntity);
            return true;
        }

    }

    private boolean replaceExistingTimesheet(Timesheet existingEntry, TimesheetDto newTimesheetDto) throws TimesheetConcurrencyException {

        if (existingEntry.getRecordedAt().isBefore(newTimesheetDto.getRecordedAt())) {
            log.info("The existing timesheet {} was recorded before the new timesheet entry {} - replace with new data!", existingEntry, newTimesheetDto);
            existingEntry.setRecordedAt(newTimesheetDto.getRecordedAt());
            existingEntry.setRecordedBy(newTimesheetDto.getRecordedBy());
            existingEntry.setWorkedHours(newTimesheetDto.getWorkedHours());

            try {
                simulateSlowDatabase(simulateDatabaseDelay);
                timesheetRepository.saveAndFlush(existingEntry);
                return true;
            } catch (ObjectOptimisticLockingFailureException e) {
                throw new TimesheetConcurrencyException(existingEntry.getId().toString(), existingEntry.getEmployeeId().toString(), existingEntry.getWorkDate());
            }
        } else {
            log.debug("Dont replace existing {} timesheet entry", existingEntry);
            return false;
        }

    }

    private void simulateSlowDatabase(boolean simulateDatabaseDelayEnabled) {
        if (simulateDatabaseDelayEnabled) {
            log.info("Simulate slow database delay enabled- entering 10 second sleep now...");
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }



}
