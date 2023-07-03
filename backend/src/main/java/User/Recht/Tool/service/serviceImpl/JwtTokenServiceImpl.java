package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.DeviceInfosDto;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.service.ClaimsOfUserService;
import User.Recht.Tool.service.JwtTokenService;
import io.smallrye.jwt.build.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;


@RequestScoped
public class JwtTokenServiceImpl implements JwtTokenService {

    @Inject
    ClaimsOfUserService claimsOfUser;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenServiceImpl.class);
    private static final String ISSUER = "USER_RECHT_TOOL";

    @Override
    public String createToken(User user, DeviceInfosDto deviceInfos, Boolean withClaims)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {


        if (withClaims) {
            return claimsOfUser.createUserClaims(user, deviceInfos);
        } else {
            String privateKeyLocation = "/privatekey.pem";
            PrivateKey privateKey = readPrivateKey(privateKeyLocation);

            return Jwt.issuer(ISSUER)
                    .upn(user.getEmail())
                    .groups("USER")
                    .subject(user.getEmail())
                    .issuedAt(currentTimeInMins())
                    .expiresIn(currentTimeInMins() + 400)
                    .sign(privateKey);
        }


    }

    /**
     * readPrivateKey - decodePrivateKey - toEncodedByte AND removeBeginEnd ARE COPIED FROM
     * https://ard333.medium.com/authentication-and-authorization-using-jwt-on-quarkus-aca1f844996a
     **/


    @Override
    public PrivateKey readPrivateKey(final String pemResName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try (InputStream contentIS = JwtTokenServiceImpl.class.getResourceAsStream(pemResName)) {
            byte[] tmp = new byte[4096];
            int length = contentIS.read(tmp);
            return decodePrivateKey(new String(tmp, 0, length, StandardCharsets.UTF_8));
        }
    }

    private static PrivateKey decodePrivateKey(final String pemEncoded) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedBytes = toEncodedBytes(pemEncoded);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    public static byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);
        return Base64.getDecoder().decode(normalizedPem);
    }

    public static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }

    static long currentTimeInMins() {
        long currentTimeMS = System.currentTimeMillis();
        return currentTimeMS / 60000;
    }

}
