package virtual.blockchain.bsystem;

import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class BlockBuilder {
    public static int difficulty = 3;

    public static Block createBlock(String message, String previousBlockHash, AtomicInteger nonce) {
        final long timestamp = new Date().getTime();
        return Block.builder()
                .currentBlockHash(setCurrentBlockHash(message, previousBlockHash, timestamp, nonce))
                .previousBlockHash(previousBlockHash)
                .message(message)
                .timestamp(timestamp)
                .nonce(nonce)
                .build();
    }

    public static Block setCurrentBlockHashDuringMining(Block block) {
        final String message = block.getMessage();
        final String previousBlockHash = block.getPreviousBlockHash();
        final long timestamp = block.getTimestamp();
        final AtomicInteger nonce = block.getNonce();

        AtomicInteger incrementedNonce = new AtomicInteger(nonce.getAndIncrement());

        return Block.builder()
                .nonce(incrementedNonce)
                .currentBlockHash(setCurrentBlockHash(message, previousBlockHash, timestamp, incrementedNonce))
                .previousBlockHash(previousBlockHash)
                .message(message)
                .timestamp(timestamp)
                .build();
    }

    public static Block mineBlock(Block block) {
        while (calculateIfCurrentBlockHashNotEqualsTarget(block)) {
            block = setCurrentBlockHashDuringMining(block);
        }

        log.info("Block Mined: ", block.getCurrentBlockHash());
        System.out.println("Block mined: " + block.getCurrentBlockHash());
        return block;
    }

    private static boolean calculateIfCurrentBlockHashNotEqualsTarget(Block block) {
        return !generateCurrentHashFromDifficulty(block).equals(generateTarget());
    }

    public static String generateCurrentBlockHash(Block block) throws InterruptedException, TimeoutException, ExecutionException {
        return generateCurrentBlockHash(block.getMessage(), block.getPreviousBlockHash(), block.getTimestamp(), block.getNonce().get());
    }

    private static String generateCurrentBlockHash(String message, String previousBlockHash, long timestamp, int nonce) throws InterruptedException, TimeoutException, ExecutionException {
        return DigitalSignature.applySha256CyptoAlgorithm(concatenateBlockData(message, previousBlockHash, timestamp, nonce)).get(60, TimeUnit.SECONDS);
    }

    private static String generateTarget() {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    private static String generateCurrentHashFromDifficulty(Block block) {
        return block.getCurrentBlockHash().substring(0, difficulty);
    }

    private static String concatenateBlockData(String message, String previousBlockHash, long timestamp, int nonce) {
        return String.join(previousBlockHash, Long.toString(timestamp), message, Integer.toString(nonce));
    }

    private static String setCurrentBlockHash(String message, String previousBlockHash, long timestamp, AtomicInteger nonce) {
        try {
            return generateCurrentBlockHash(message, previousBlockHash, timestamp, nonce.get());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error generating current cryptographic Block hash: {}", e);
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            log.error("Timeout during generation of current cryptographic Block hash: {}", e);
            throw new RuntimeException(e);
        }
    }
}
