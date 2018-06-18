package virtual.blockchain.bsystem.block;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Getter
@Builder
public class Block {
    private final String previousBlockHash;
    private final String message;
    private final long timestamp;
    private final String currentBlockHash;
    private final long blockHeight;
    private final AtomicInteger nonce;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Block)) {
            return false;
        }

        Block b = (Block) o;

        return Objects.equals(currentBlockHash, b.currentBlockHash)
                && Objects.equals(previousBlockHash, b.previousBlockHash)
                && Objects.equals(timestamp, b.timestamp)
                && Objects.equals(message, b.message)
                && Objects.equals(blockHeight, b.blockHeight);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = generateResult(result, currentBlockHash);
        result = generateResult(result, previousBlockHash);
        result = generateResult(result, timestamp);
        result = generateResult(result, message);
        result = generateResult(result, blockHeight);
        return result;
    }

    private <T> int generateResult(int result, T t) {
        return getPrimeNum() * result + ((t == null) ? 0 : t.hashCode());
    }

    private int getPrimeNum(){
        return 67;
    }
}