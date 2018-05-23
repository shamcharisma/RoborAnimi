package virtual.blockchain.cryptography;

import com.google.common.io.BaseEncoding;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import javax.crypto.KeyGenerator;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static virtual.blockchain.cryptography.CyptoConstants.BOUNCY_CASTLE;
import static virtual.blockchain.cryptography.CyptoConstants.ECDH;
import static virtual.blockchain.cryptography.CyptoConstants.KEY_GENERATOR_ALGORITHM;
import static virtual.blockchain.cryptography.CyptoConstants.OPERATING_SYSTEM_NAME_ATTR;
import static virtual.blockchain.cryptography.CyptoConstants.PRIME192V1;
import static virtual.blockchain.cryptography.CyptoConstants.PRIVATE_KEY_BYTES;
import static virtual.blockchain.cryptography.CyptoConstants.PRIVATE_KEY_SIZE;
import static virtual.blockchain.cryptography.CyptoConstants.UNIX_FIX;
import static virtual.blockchain.cryptography.CyptoConstants.WINDOWS_FIX;
import static virtual.blockchain.cryptography.CyptoConstants.WINDOWS_PSEUDO_RANDOM_NUM_GENERATOR;

/*
*  Contains separate implementation for Windows-portability. For Windows, Default SHA1PRNG with self-seeding is dangerously insecure.
*  On the contrary, Default Unix implementation with self-seeding in NativePRNG is secure and safe.
*  TODO: Need to optimize it
*/
@Log4j2
class PrivateKeyGenerator {
    private final String operatingSystemName;
    private final byte[] privateKeyBytes;

    PrivateKeyGenerator() {
        operatingSystemName = System.getProperty(OPERATING_SYSTEM_NAME_ATTR);
        privateKeyBytes = new byte[PRIVATE_KEY_BYTES];
        Security.addProvider(new BouncyCastleProvider());
    }

    Supplier<SecureRandom> generatePrivateKeyV1() {
        final SecureRandom secureRandom;

        try {
            secureRandom = getSecureRandomBasedOnOperatingSystem(isUnixBasedOS(), isWindowsBasedOS(),
                    getUnixSecureRandom(), getWindowsSecureRandom());

            if (secureRandom == null) {
                throw new RuntimeException("Error generating Private Key");
            }

            secureRandom.nextBytes(privateKeyBytes);
            return () -> secureRandom;
        } catch (NoSuchAlgorithmException e) {
            log.error("Error generating Private Key");
            throw new RuntimeException(e);
        }
    }

    String getEncodedKey(SecureRandom rand) {
        final KeyGenerator keyGenerator = keyGenerator(rand);
        return Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded());
    }

    private KeyGenerator keyGenerator(SecureRandom rand) {
        final KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KEY_GENERATOR_ALGORITHM);
            keyGenerator.init(PRIVATE_KEY_SIZE, rand);

            return keyGenerator;
        } catch (NoSuchAlgorithmException e) {
            log.error("Error generating Secure Key Generator");
            throw new RuntimeException(e);
        }
    }

    private SecureRandom getSecureRandomBasedOnOperatingSystem(boolean unixOsTest, boolean unixWindowsTest,
                                                               Supplier<SecureRandom> t, Supplier<SecureRandom> f) {
        SecureRandom secureRandom = null;
        if (unixOsTest) {
            secureRandom = t.get();
        } else if (unixWindowsTest) {
            secureRandom = f.get();
        }

        return secureRandom;
    }

    private boolean isUnixBasedOS() {
        return checkOS(UNIX_FIX);
    }

    private boolean isWindowsBasedOS() {
        return checkOS(WINDOWS_FIX);
    }

    private boolean checkOS(String[] operatingSysList) {
        Predicate<String> osPredicate = (x) -> Arrays.stream(operatingSysList).anyMatch(x::contains);
        return osPredicate.test(operatingSystemName.toLowerCase());
    }

    private Supplier<SecureRandom> getUnixSecureRandom() {
        return SecureRandom::new;
    }

    private Supplier<SecureRandom> getWindowsSecureRandom() throws NoSuchAlgorithmException {
        return () -> {
            try {
                return SecureRandom.getInstance(WINDOWS_PSEUDO_RANDOM_NUM_GENERATOR);
            } catch (NoSuchAlgorithmException e) {
                log.error("Error generating Secure Key Generator");
                throw new RuntimeException(e);
            }
        };
    }

    /* V2 Private Key generation
    *  This is second-pass at private key generation using elliptic curve cryptography
    */
    ECPrivateKey generatePrivateKey() {
        try {
            final KeyPair keyPair = generateKeyPair();

            return (ECPrivateKey) keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm found while generating private key");
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            log.error("No such provider found found while generating private key");
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            log.error("Invalid algorithm parameters found while generating private key");
            throw new RuntimeException(e);
        }
    }

    private KeyPair generateKeyPair() throws InvalidAlgorithmParameterException, NoSuchProviderException, NoSuchAlgorithmException {
        final ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(PRIME192V1);
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ECDH, BOUNCY_CASTLE);

        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public static String convertPrivateKeyToHex(byte[] privateKeyInBytes) {
        return BaseEncoding.base64().encode(privateKeyInBytes);
    }
}