package audit.logs.service;

import audit.logs.entity.Log;
import audit.logs.exception.DateException;
import audit.logs.exception.PermissionDeniedException;

import java.util.List;

public interface LogService {
    Log saveLog(String action , String token);

    List<Log> getLogs(String userId, String from, String to, String action) throws DateException;

    void deleteLogs(String userId, String from, String to, String action) throws DateException;

    void checkPermission(String token,String permission) throws PermissionDeniedException ;
}
