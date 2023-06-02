package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.entity.Role;
import User.Recht.Tool.service.JwtTokenService;
import User.Recht.Tool.entity.User;

import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.eclipse.microprofile.jwt.Claims;
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
import java.util.HashSet;
import java.util.Set;

@RequestScoped
public class JwtTokenServiceImpl implements JwtTokenService {
    private static final long TOKEN_EXPIRE_IN = 243200;
    private static final String ISSUER = "USER_RECHT_TOOL";

    @Inject
    ClaimsOfUserImpl claimsOfUser;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenServiceImpl.class);

    @Override
    public String createToken(User user, String ipAddress, String deviceName)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        JwtClaimsBuilder claims= claimsOfUser.createUserClaims( user, ipAddress, deviceName);

        String privateKeyLocation = "/privatekey.pem";
        PrivateKey privateKey = readPrivateKey(privateKeyLocation);

        long currentTimeInSecs = currentTimeInSecs();
        claims.issuer(ISSUER);
        claims.issuedAt(currentTimeInSecs);
        claims.claim(Claims.auth_time.name(), currentTimeInSecs);
        claims.expiresAt(currentTimeInSecs + TOKEN_EXPIRE_IN);
        claims.groups("USER");

        return claims.jws().signatureKeyId(privateKeyLocation).sign(privateKey);
    }

    private static PrivateKey readPrivateKey(final String pemResName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
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


        private static int currentTimeInSecs() {
            long currentTimeMS = System.currentTimeMillis();
            return (int) (currentTimeMS / 1000);
        }
}
