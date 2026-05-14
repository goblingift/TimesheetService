package gift.goblin.timesheet.jpa.repo;

import gift.goblin.timesheet.jpa.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TimesheetRepository extends JpaRepository<Timesheet, UUID> {
    List<Timesheet> getTimesheetByEmployeeIdAndWorkDate(Integer employeeId, LocalDate workDate);
}
