package User.Recht.Tool.service;

import User.Recht.Tool.dtos.DeviceInfosDto;
import User.Recht.Tool.entity.User;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

public interface JwtTokenService {
    String createToken (User user, DeviceInfosDto deviceInfos,Boolean withClaims)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;

    PrivateKey readPrivateKey(String pemResName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;
}
