package gift.goblin.timesheet.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetDto {

    private UUID id;

    @NotNull
    private Integer employeeId;

    @NotNull
    private Instant recordedAt;

    @NotNull
    private Integer recordedBy;

    @NotNull
    private LocalDate workDate;

    @NotNull
    @DecimalMin("0.25")
    @DecimalMax("24.00")
    @Digits(integer = 2, fraction = 2)
    private BigDecimal workedHours;

    private Instant createdAt;

}
