package audit.logs.service;

import audit.logs.entity.Log;
import audit.logs.exception.DateException;
import audit.logs.exception.PermissionDeniedException;

import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;

public interface LogService {
    Log saveLog(String action , String token);

    List<Log> getLogs(String userId, String from, String to, String action) throws DateException;

    void deleteLogs(String userId, String from, String to, String action) throws DateException;

    @Transactional
    void deleteLogById(Long id) throws NotFoundException;

    void checkPermission(String token, String permission) throws PermissionDeniedException ;
}
