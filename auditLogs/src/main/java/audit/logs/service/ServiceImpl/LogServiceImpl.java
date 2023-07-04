package audit.logs.service.ServiceImpl;

import audit.logs.entity.Log;
import audit.logs.exception.DateException;
import audit.logs.exception.PermissionDeniedException;
import audit.logs.repository.LogRepository;
import audit.logs.service.LogService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.eclipse.microprofile.jwt.Claims.groups;
import static org.eclipse.microprofile.jwt.Claims.upn;

@RequestScoped
public class LogServiceImpl implements LogService {

    @Inject
    LogRepository logRepository;

    @Inject
    EntityManager entityManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);

    @Override
    @Transactional
    public Log saveLog(String action , String token) {
        Map<String, Object> map = listClaimUsingJWT(token);

        String userEmail = (String) map.get("upn");
        String osVersion = (String) map.get("osVersion");
        String userAgent = (String) map.get("userAgent");
        String clientIpAddress = (String) map.get("clientIpAddress");
        String osName = (String) map.get("osName");
        boolean isVerifiedEmail = (boolean) map.get("isVerifiedEmail");
        String userId = (String) map.get("userId");

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);
        currentDate = LocalDate.parse(formattedDate, formatter);

        Log log= new Log(userId,  userEmail, currentDate, action.toUpperCase(), osName,
                osVersion, userAgent, clientIpAddress, isVerifiedEmail);
        logRepository.persist(log);

        return log;
    }

    @Override
    public List<Log> getLogs(String userId, String from, String to, String action) throws DateException {
        List<Log> logs = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate fromDate = null;
        if (from != null) {
            fromDate = LocalDate.parse(from, formatter);
        }

        LocalDate toDate = null;
        if (to != null) {
            toDate = LocalDate.parse(to, formatter);
        }
        // Build the SQL query
        StringBuilder queryBuilder = new StringBuilder("SELECT l FROM Log l WHERE 1=1");
        Map<String, Object> queryParams = new HashMap<>();

        if (userId != null) {
            queryBuilder.append(" AND l.userId = :userId");
            queryParams.put("userId", userId);
        }

        if (from != null) {
            queryBuilder.append(" AND l.date >= :fromDate");
            queryParams.put("fromDate", fromDate);
        }

        if (to != null) {
            queryBuilder.append(" AND l.date <= :toDate");
            queryParams.put("toDate", toDate);
        }

        if (action != null) {
            queryBuilder.append(" AND l.action = :action");
            queryParams.put("action", action.toUpperCase());
        }

        try {
            TypedQuery<Log> query = entityManager.createQuery(queryBuilder.toString(), Log.class);
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            logs = query.getResultList();
        } catch (Exception e) {
        }

        return logs;
    }

    @Override
    @Transactional
    public void deleteLogs(String userId, String from, String to, String action) throws DateException {

        StringBuilder queryBuilder = new StringBuilder("DELETE FROM Log l WHERE 1=1");
        Map<String, Object> queryParams = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate fromDate = null;
        if (from != null) {
            fromDate = LocalDate.parse(from, formatter);
        }

        LocalDate toDate = null;
        if (to != null) {
            toDate = LocalDate.parse(to, formatter);
        }

        if (userId != null) {
            queryBuilder.append(" AND l.userId = :userId");
            queryParams.put("userId", Long.parseLong(userId));
        }

        if (from != null) {
            queryBuilder.append(" AND l.date >= :fromDate");
            queryParams.put("fromDate", fromDate);
        }

        if (to != null) {
            queryBuilder.append(" AND l.date <= :toDate");
            queryParams.put("toDate", toDate);
        }

        if (action != null) {
            queryBuilder.append(" AND l.action = :action");
            queryParams.put("action", action.toUpperCase());
        }

        try {
            Query query = entityManager.createQuery(queryBuilder.toString());
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            int deletedCount = query.executeUpdate();
        } catch (Exception e) {
        }
    }


    @Override
    public void checkPermission(String token,String permission) throws PermissionDeniedException {
        Map<String, Object> map = listClaimUsingJWT(token);
        List<String> listPermission = (List<String> ) map.get("allPermissionsOfUser");
        String type = (String) map.get("type");

        if(!type.equals("USER") && !listPermission.contains(permission)){
            throw new PermissionDeniedException("PERMISSION DENIED");
        }

    }

    public Map<String, Object> listClaimUsingJWT(String accessToken){
        Map<String, Object> map = new HashMap<>();

        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            Map<String, Object> myClaim = claimsSet.getClaims();

            String[] keySet = myClaim.keySet().toArray(new String[0]);

            for (String s : keySet) {
                map.put(s, myClaim.get(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }

}
