package User.Recht.Tool.exception.user;

import User.Recht.Tool.exception.DuplicateElementException;

public class UserNameDuplicateElementException extends DuplicateElementException {
    public UserNameDuplicateElementException(String reason) {
        super(reason);
    }
}
