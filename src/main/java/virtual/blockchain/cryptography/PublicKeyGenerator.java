package virtual.blockchain.cryptography;

import com.google.common.io.BaseEncoding;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.interfaces.ECPrivateKey;

import static virtual.blockchain.cryptography.CyptoConstants.COMPRESSED_SIGN_BYTE;
import static virtual.blockchain.cryptography.CyptoConstants.PRIVATE_KEY_BYTES;
import static virtual.blockchain.cryptography.CyptoConstants.PUBLIC_KEY_BYTES;
import static virtual.blockchain.cryptography.CyptoConstants.SECP256R1;

@Log4j2
public class PublicKeyGenerator {
    private final ECPrivateKey privateKey;

    public PublicKeyGenerator(ECPrivateKey privateKey){
        this.privateKey = privateKey;
    }

    /*
     * Returns public key point from the given private key
     * TODO: Add Completable future with Akka and handle threading issues
     */
    String getPublicKey() {
        final X9ECParameters ecp = SECNamedCurves.getByName(SECP256R1);
        final ECPoint ellipticCurvePoint = ecp.getG().multiply(privateKey.getS());
        final EllipticCurveXYBytes ellipticCurveXYBytes = getXAndYCoordBytes(ellipticCurvePoint);
        return generateCompressedPublicKeyBytes(ellipticCurveXYBytes);
    }

    private EllipticCurveXYBytes getXAndYCoordBytes(ECPoint ellipticCurvePoint) {
        final BigInteger x = ellipticCurvePoint.getXCoord().toBigInteger();
        final BigInteger y = ellipticCurvePoint.getYCoord().toBigInteger();

        final byte[] xBytes = this.removeSignByte(x.toByteArray());
        final byte[] yBytes = this.removeSignByte(y.toByteArray());

        return EllipticCurveXYBytes.builder()
                .xCoordBytes(xBytes)
                .yCoordBytes(yBytes)
                .build();
    }

    private String generateCompressedPublicKeyBytes(EllipticCurveXYBytes ellipticCurveXYBytes) {
        final byte[] pubKeyBytes = new byte[PUBLIC_KEY_BYTES];
        pubKeyBytes[0] = new Byte(COMPRESSED_SIGN_BYTE);

        byte[] xCoordBytes = ellipticCurveXYBytes.xCoordBytes;
        byte[] yCoordBytes = ellipticCurveXYBytes.yCoordBytes;

        System.arraycopy(xCoordBytes, 0, pubKeyBytes, 1, xCoordBytes.length);
        System.arraycopy(yCoordBytes, 0, pubKeyBytes, PRIVATE_KEY_BYTES + 1, yCoordBytes.length);

        return convertPrivateKeyToHex(pubKeyBytes);
    }

    private byte[] removeSignByte(byte[] privateKey) {
        if (privateKey.length == 33) {
            byte[] newArr = new byte[32];
            System.arraycopy(privateKey, 1, newArr, 0, newArr.length);
            return newArr;
        }
        return privateKey;
    }

    public static String convertPrivateKeyToHex(byte[] privateKeyInBytes) {
        return BaseEncoding.base64().encode(privateKeyInBytes);
    }
}
