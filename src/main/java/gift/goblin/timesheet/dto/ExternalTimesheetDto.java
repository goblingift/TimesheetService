package gift.goblin.timesheet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalTimesheetDto {
    private Integer employeeId;
    private Instant recordedAt;
    private Integer recordedBy;
    private Integer workDateDay;
    private Integer workDateMonth;
    private Integer workDateYear;
    private BigDecimal workedHours;
}
