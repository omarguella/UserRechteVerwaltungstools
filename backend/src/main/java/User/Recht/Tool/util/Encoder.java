package User.Recht.Tool.util;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.ApplicationScoped;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@ApplicationScoped
public class Encoder {

    @ConfigProperty(name = "User.Recht.Tool.password.secret")
    private String secret;
    @ConfigProperty(name = "User.Recht.Tool.password.iteration")
    private Integer iteration;
    @ConfigProperty(name = "User.Recht.Tool.password.length")
    private Integer length;

    public String passwordCoder(CharSequence cs) {
        try {
            //BCrypt anstatt PBKDF2
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA384")
                    .generateSecret(
                            new PBEKeySpec(cs.toString().toCharArray(), secret.getBytes(), iteration, length))
                    .getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }
}

