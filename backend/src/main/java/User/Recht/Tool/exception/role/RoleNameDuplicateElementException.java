package User.Recht.Tool.exception.role;

import User.Recht.Tool.exception.DuplicateElementException;

public class RoleNameDuplicateElementException extends DuplicateElementException {
    public RoleNameDuplicateElementException(String reason) {
        super(reason);
    }
}
