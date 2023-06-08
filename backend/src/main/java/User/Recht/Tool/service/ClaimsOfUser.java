package User.Recht.Tool.service;

import User.Recht.Tool.dtos.DeviceInfosDto;
import User.Recht.Tool.entity.User;
import io.smallrye.jwt.build.JwtClaimsBuilder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public interface ClaimsOfUser {
    String createUserClaims(User user, DeviceInfosDto deviceInfos) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;

    Map<String, Object> listClaimUsingJWT(String accessToken);
}
