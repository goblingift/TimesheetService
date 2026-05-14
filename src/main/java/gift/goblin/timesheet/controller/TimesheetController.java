package gift.goblin.timesheet.controller;

import gift.goblin.timesheet.dto.TimesheetDto;
import gift.goblin.timesheet.jpa.repo.TimesheetRepository;
import gift.goblin.timesheet.service.TimesheetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/timesheets")
@RequiredArgsConstructor
public class TimesheetController {

    private final TimesheetService timesheetService;

    @PostMapping
    public ResponseEntity<String> createTimesheet(@Valid @RequestBody TimesheetDto timesheetDto) {
        boolean savedTimesheet = timesheetService.saveTimesheet(timesheetDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(savedTimesheet));
    }

}
