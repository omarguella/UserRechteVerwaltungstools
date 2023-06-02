package User.Recht.Tool.service;

import User.Recht.Tool.entity.User;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface JwtTokenService {
    String createToken (User user, String ipAddress, String deviceName)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;
}
