package virtual.blockchain.cryptography;

import com.google.common.io.BaseEncoding;

class CryptoUtils {
    static String convertPrivateKeyToHex(byte[] privateKeyInBytes) {
        return BaseEncoding.base64().encode(privateKeyInBytes);
    }
}
