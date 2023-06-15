package User.Recht.Tool.service;

import User.Recht.Tool.dtos.auditLogDto.LogDto;
import User.Recht.Tool.exception.logs.DateException;

import java.util.List;

public interface LogsService {
    void saveLogs(String action, String token);

    List<LogDto> getLogs(String userId, String from, String to, String action, String token) throws DateException;
    void deleteLogs(String userId, String from, String to, String action, String token) throws DateException;
}
