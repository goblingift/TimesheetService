package gift.goblin.timesheet.exception;

import java.time.LocalDate;

public class TimesheetConcurrencyException extends RuntimeException {

    public TimesheetConcurrencyException(String id, String employeeId, LocalDate workDate) {
        super("Timesheet concurrency exception occurred, while updating this entity: id=" + id + ", employeeId=" + employeeId + ", workDate=" + workDate);
    }

}
