package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.auditLogs.api.LogsAPI;
import User.Recht.Tool.dtos.auditLogDto.LogDto;
import User.Recht.Tool.exception.logs.DateException;
import User.Recht.Tool.service.LogsService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RequestScoped
public class LogsServiceImpl implements LogsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogsServiceImpl.class);

    @Inject
    @RestClient
    LogsAPI logsAPI;

    @Override
    public void saveLogs(String action, String token) {
        LogDto log = logsAPI.saveLog(action, token);

    }


    @Override
    public List<LogDto> getLogs(String userId, String from, String to, String action, String token) throws DateException {


        if (userId != null && userId.isEmpty()) {
            userId = null;
        }

        if (from != null && from.isEmpty()) {
            from = null;
        }
        if (to != null && to.isEmpty()) {
            to = null;
        }
        if (action != null && action.isEmpty()) {
            action = null;
        }

        if (from != null) {
            isDateValid(from);
        }
        if (to != null) {
            isDateValid(to);
        }


        return logsAPI.getLogs(userId, from, to, action, token);

    }


    @Override
    public void deleteLogs(String userId, String from, String to, String action, String token) throws DateException {

        if (userId != null && userId.isEmpty()) {
            userId = null;
        }

        if (from != null && from.isEmpty()) {
            from = null;
        }
        if (to != null && to.isEmpty()) {
            to = null;
        }
        if (action != null && action.isEmpty()) {
            action = null;
        }

        if (from != null) {
            isDateValid(from);
        }
        if (to != null) {
            isDateValid(to);
        }
        logsAPI.deleteLogs(userId, from, to, action, token);

    }


    public static void isDateValid(String dateString) throws DateException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new DateException("THE DATE VALUE SHOULD BI IN THIS FORM dd-MM-yyyy");
        }
    }


}
