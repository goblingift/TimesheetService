package gift.goblin.timesheet.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "timesheets",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_timesheet_employee_workdate", columnNames = {"employee_id", "work_date"})
        }
)
public class Timesheet {

    @Id
    @GeneratedValue
    private UUID id;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private Integer employeeId;

    /**
     * Contains the timestamp, when the user has submitted/maintained that timesheet entry.
     */
    @Column(nullable = false)
    private Instant recordedAt;

    /**
     * Contains the employeeId, of the user who has submitted/maintained that timesheet entry.
     */
    @Column(nullable = false)
    private Integer recordedBy;

    @Column(nullable = false)
    private LocalDate workDate;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal workedHours;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

}
