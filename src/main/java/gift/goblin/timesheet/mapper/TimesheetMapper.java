package gift.goblin.timesheet.mapper;

import gift.goblin.timesheet.dto.ExternalTimesheetDto;
import gift.goblin.timesheet.dto.TimesheetDto;
import gift.goblin.timesheet.jpa.entity.Timesheet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TimesheetMapper {

    public TimesheetDto toDto(Timesheet entity) {
        if (entity == null) {
            return null;
        }

        return new TimesheetDto(
                entity.getId(),
                entity.getEmployeeId(),
                entity.getRecordedAt(),
                entity.getRecordedBy(),
                entity.getWorkDate(),
                entity.getWorkedHours(),
                entity.getCreatedAt()
        );
    }

    public TimesheetDto toDto(ExternalTimesheetDto externalTimesheetDto) {
        if (externalTimesheetDto == null) {
            return null;
        }
        TimesheetDto timesheetDto = new TimesheetDto();
        timesheetDto.setEmployeeId(externalTimesheetDto.getEmployeeId());
        timesheetDto.setRecordedAt(externalTimesheetDto.getRecordedAt());
        timesheetDto.setRecordedBy(externalTimesheetDto.getRecordedBy());
        timesheetDto.setWorkDate(LocalDate.of(
                externalTimesheetDto.getWorkDateYear(),
                externalTimesheetDto.getWorkDateMonth(),
                externalTimesheetDto.getWorkDateDay()));
        timesheetDto.setWorkedHours(externalTimesheetDto.getWorkedHours());
        return timesheetDto;
    }

    public Timesheet toEntity(TimesheetDto dto) {
        if (dto == null) {
            return null;
        }

        Timesheet entity = new Timesheet();
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setRecordedAt(dto.getRecordedAt());
        entity.setRecordedBy(dto.getRecordedBy());
        entity.setWorkDate(dto.getWorkDate());
        entity.setWorkedHours(dto.getWorkedHours());
        return entity;
    }

}
