package User.Recht.Tool.service;

import User.Recht.Tool.entity.User;
import io.smallrye.jwt.build.JwtClaimsBuilder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface ClaimsOfUser {
    String createUserClaims(User user, String ipAddress, String deviceName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;
}
