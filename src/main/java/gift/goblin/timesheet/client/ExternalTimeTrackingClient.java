package gift.goblin.timesheet.client;


import gift.goblin.timesheet.dto.ExternalTimesheetDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ExternalTimeTrackingClient {

    private final RestClient restClient;
    private final String externalApiBaseUrl;
    private final String path;

    public ExternalTimeTrackingClient(@Value("${external.timesheet.api.base-url}") String externalApiBaseUrl,
                                      @Value("${external.timesheet.api.resource-path}") String path) {
        this.restClient = RestClient.create(externalApiBaseUrl);
        this.externalApiBaseUrl = externalApiBaseUrl;
        this.path = path;
    }

    public List<ExternalTimesheetDto> getExternalTimesheets(int month, int year) {

        List<ExternalTimesheetDto> fetchedTimesheets = List.of();

        log.info("Fetch external timesheets from {} {} with month {} and year {}", externalApiBaseUrl, path, month, year);
        try {
            ExternalTimesheetDto[] timesheets = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .queryParam("month", month)
                            .queryParam("year", year)
                            .build())
                    .retrieve()
                    .body(ExternalTimesheetDto[].class);
            if (timesheets == null) {
                log.warn("Could not get any timesheets from external resource {}{}", externalApiBaseUrl, path);
            } else {
                log.info("Successful fetched {} timesheets", timesheets.length);
                fetchedTimesheets  = Arrays.asList(timesheets);
            }

        } catch (RestClientException e) {
            log.error("Exception thrown while calling external timesheet-api {}", externalApiBaseUrl, e);
        }
        return fetchedTimesheets;
    }

}
