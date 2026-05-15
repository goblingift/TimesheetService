package gift.goblin.timesheet.service;

import gift.goblin.timesheet.dto.TimesheetDto;
import gift.goblin.timesheet.exception.TimesheetConcurrencyException;
import gift.goblin.timesheet.jpa.entity.Timesheet;
import gift.goblin.timesheet.jpa.repo.TimesheetRepository;
import gift.goblin.timesheet.mapper.TimesheetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimesheetServiceTest {

    @Mock
    private TimesheetRepository timesheetRepository;

    @Mock
    private TimesheetMapper timesheetMapper;

    @InjectMocks
    private TimesheetService timesheetService;

    private TimesheetDto dto;

    @BeforeEach
    void setUp() {
        dto = new TimesheetDto();
        dto.setEmployeeId(1);
        dto.setRecordedAt(Instant.parse("2026-05-15T10:00:00Z"));
        dto.setRecordedBy(99);
        dto.setWorkDate(LocalDate.of(2026, 5, 15));
        dto.setWorkedHours(new BigDecimal("2.50"));
    }

    @Test
    void saveTimesheet_shouldCreateNewEntry_whenNoExistingEntryExists() throws Exception {
        when(timesheetRepository.getTimesheetByEmployeeIdAndWorkDate(1, dto.getWorkDate()))
                .thenReturn(List.of());

        Timesheet entity = new Timesheet();
        entity.setEmployeeId(1);
        entity.setWorkDate(dto.getWorkDate());
        when(timesheetMapper.toEntity(dto)).thenReturn(entity);
        when(timesheetRepository.saveAndFlush(entity)).thenReturn(entity);

        boolean result = timesheetService.saveTimesheet(dto);

        assertThat(result).isTrue();
        verify(timesheetMapper).toEntity(dto);
        verify(timesheetRepository).saveAndFlush(entity);
    }

    @Test
    void saveTimesheet_shouldReplaceExistingEntry_whenNewRecordIsNewer() throws Exception {
        Timesheet existing = new Timesheet();
        existing.setId(UUID.randomUUID());
        existing.setEmployeeId(1);
        existing.setWorkDate(dto.getWorkDate());
        existing.setRecordedAt(Instant.parse("2026-05-15T09:00:00Z"));

        Timesheet updated = new Timesheet();
        updated.setId(existing.getId());
        updated.setEmployeeId(1);
        updated.setWorkDate(dto.getWorkDate());
        updated.setRecordedAt(dto.getRecordedAt());

        when(timesheetRepository.getTimesheetByEmployeeIdAndWorkDate(1, dto.getWorkDate()))
                .thenReturn(List.of(existing));
        when(timesheetRepository.saveAndFlush(any(Timesheet.class))).thenReturn(updated);

        boolean result = timesheetService.saveTimesheet(dto);

        assertThat(result).isTrue();
        verify(timesheetRepository).saveAndFlush(existing);
        assertThat(existing.getRecordedAt()).isEqualTo(dto.getRecordedAt());
        assertThat(existing.getRecordedBy()).isEqualTo(dto.getRecordedBy());
        assertThat(existing.getWorkedHours()).isEqualByComparingTo(dto.getWorkedHours());
    }

    @Test
    void saveTimesheet_shouldNotReplaceExistingEntry_whenExistingRecordIsNewer() throws Exception {
        Timesheet existing = new Timesheet();
        existing.setEmployeeId(1);
        existing.setWorkDate(dto.getWorkDate());
        existing.setRecordedAt(Instant.parse("2026-05-15T11:00:00Z"));

        when(timesheetRepository.getTimesheetByEmployeeIdAndWorkDate(1, dto.getWorkDate()))
                .thenReturn(List.of(existing));

        boolean result = timesheetService.saveTimesheet(dto);

        assertThat(result).isFalse();
        verify(timesheetRepository, never()).saveAndFlush(any());
    }

    @Test
    void saveTimesheet_shouldThrowConcurrencyException_whenOptimisticLockFails() throws Exception {
        Timesheet existing = new Timesheet();
        existing.setId(UUID.randomUUID());
        existing.setEmployeeId(1);
        existing.setWorkDate(dto.getWorkDate());
        existing.setRecordedAt(Instant.parse("2026-05-15T09:00:00Z"));

        when(timesheetRepository.getTimesheetByEmployeeIdAndWorkDate(1, dto.getWorkDate()))
                .thenReturn(List.of(existing));
        doThrow(new ObjectOptimisticLockingFailureException(Timesheet.class, existing.getId()))
                .when(timesheetRepository).saveAndFlush(existing);

        assertThatThrownBy(() -> timesheetService.saveTimesheet(dto))
                .isInstanceOf(TimesheetConcurrencyException.class);

        verify(timesheetRepository).saveAndFlush(existing);
    }
}