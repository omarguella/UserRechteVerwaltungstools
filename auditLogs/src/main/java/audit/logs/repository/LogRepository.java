package audit.logs.repository;

import audit.logs.entity.Log;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogRepository implements PanacheRepository<Log> {


}
