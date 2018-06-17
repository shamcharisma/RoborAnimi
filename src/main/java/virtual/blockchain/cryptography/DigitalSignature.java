package virtual.blockchain.bsystem;

import com.google.common.hash.Hashing;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Log4j2
public class DigitalSignature {
    private static final Charset unicodeEncode = StandardCharsets.UTF_8;

    public static CompletableFuture<String> applySha256CyptoAlgorithm(String originalStr) {
        return CompletableFuture.supplyAsync(() -> Hashing.sha256()
                .hashString(originalStr, unicodeEncode)
                .toString());
    }
}