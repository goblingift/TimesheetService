package gift.goblin.timesheet.mapper;

import gift.goblin.timesheet.dto.TimesheetDto;
import gift.goblin.timesheet.jpa.entity.Timesheet;
import org.springframework.stereotype.Component;

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

    public Timesheet toEntity(TimesheetDto dto) {
        if (dto == null) {
            return null;
        }

        Timesheet entity = new Timesheet();
        entity.setId(dto.getId());
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setRecordedAt(dto.getRecordedAt());
        entity.setRecordedBy(dto.getRecordedBy());
        entity.setWorkDate(dto.getWorkDate());
        entity.setWorkedHours(dto.getWorkedHours());
        return entity;
    }

}
