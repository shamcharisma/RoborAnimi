package virtual.blockchain.bsystem;

import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Log4j2
public class BlockBuilder {
    public static Block createBlock(String message, String previousBlockHash) {
        final long timestamp = new Date().getTime();
        return Block.builder()
                .currentBlockHash(setCurrentBlockHash(message, previousBlockHash, timestamp))
                .previousBlockHash(previousBlockHash)
                .message(message)
                .timestamp(timestamp)
                .build();
    }

    public static String generateCurrentBlockHash(String message, String previousBlockHash, long timestamp) throws InterruptedException, TimeoutException, ExecutionException {
        return DigitalSignature.applySha256CyptoAlgorithm(concatenateBlockData(message, previousBlockHash, timestamp)).get(60, TimeUnit.SECONDS);
    }

    private static String concatenateBlockData(String message, String previousBlockHash, long timestamp) {
        return String.join(previousBlockHash, Long.toString(timestamp), message);
    }

    private static String setCurrentBlockHash(String message, String previousBlockHash, long timestamp) {
        try {
            return generateCurrentBlockHash(message, previousBlockHash, timestamp);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error generating current cryptographic Block hash: {}", e);
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            log.error("Timeout during generation of current cryptographic Block hash: {}", e);
            throw new RuntimeException(e);
        }
    }
}
