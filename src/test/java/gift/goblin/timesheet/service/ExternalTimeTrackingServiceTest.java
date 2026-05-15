package gift.goblin.timesheet.service;

import gift.goblin.timesheet.client.ExternalTimeTrackingClient;
import gift.goblin.timesheet.dto.ExternalTimesheetDto;
import gift.goblin.timesheet.dto.TimesheetDto;
import gift.goblin.timesheet.mapper.TimesheetMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalTimeTrackingServiceTest {

    @Mock
    private TimesheetMapper timesheetMapper;

    @Mock
    private TimesheetService timesheetService;

    @Mock
    private ExternalTimeTrackingClient externalTimeTrackingClient;

    @InjectMocks
    private ExternalTimeTrackingService externalTimeTrackingService;

    @Test
    void fetchAndSaveExternalTimesheets_shouldCallExternalClient12TimesAndSaveAllMappedEntries() {
        ExternalTimesheetDto externalDto = new ExternalTimesheetDto();
        TimesheetDto mappedDto = new TimesheetDto();

        when(externalTimeTrackingClient.getExternalTimesheets(anyInt(), anyInt()))
                .thenReturn(List.of(externalDto));
        when(timesheetMapper.toDto(externalDto)).thenReturn(mappedDto);
        when(timesheetService.saveTimesheet(mappedDto)).thenReturn(true);

        externalTimeTrackingService.fetchAndSaveExternalTimesheets();

        verify(externalTimeTrackingClient, times(12)).getExternalTimesheets(anyInt(), anyInt());
        verify(timesheetMapper, times(12)).toDto(externalDto);
        verify(timesheetService, times(12)).saveTimesheet(mappedDto);
        verifyNoMoreInteractions(timesheetService);
    }

    @Test
    void fetchAndSaveExternalTimesheets_shouldContinueWhenSomeSavesReturnFalse() {
        ExternalTimesheetDto externalDto = new ExternalTimesheetDto();
        TimesheetDto mappedDto = new TimesheetDto();

        when(externalTimeTrackingClient.getExternalTimesheets(anyInt(), anyInt()))
                .thenReturn(List.of(externalDto, externalDto));
        when(timesheetMapper.toDto(externalDto)).thenReturn(mappedDto);
        when(timesheetService.saveTimesheet(mappedDto)).thenReturn(true, false);

        externalTimeTrackingService.fetchAndSaveExternalTimesheets();

        verify(timesheetService, times(24)).saveTimesheet(mappedDto);
    }

    @Test
    void fetchAndSaveExternalTimesheets_shouldDoNothingWhenAllClientResponsesAreEmpty() {
        when(externalTimeTrackingClient.getExternalTimesheets(anyInt(), anyInt()))
                .thenReturn(List.of());

        externalTimeTrackingService.fetchAndSaveExternalTimesheets();

        verify(externalTimeTrackingClient, times(12)).getExternalTimesheets(anyInt(), anyInt());
        verifyNoInteractions(timesheetMapper);
        verifyNoInteractions(timesheetService);
    }
}