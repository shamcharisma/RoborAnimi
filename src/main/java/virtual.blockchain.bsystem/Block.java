package virtual.blockchain.bsystem;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;

@Log4j2
@Getter
@Builder
public class Block {
    private final String previousBlockHash;
    private final String message;
    private final long timestamp;
    private String currentBlockHash;

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
                && Objects.equals(message, b.message);
    }

    @Override
    public int hashCode() {
        final int primeNum = 67;
        int result = 1;
        result = generateResult(primeNum, result, currentBlockHash);
        result = generateResult(primeNum, result, previousBlockHash);
        result = generateResult(primeNum, result, timestamp);
        result = generateResult(primeNum, result, message);
        return result;
    }

    private <T> int generateResult(int primeNum, int result, T t) {
        return primeNum * result + ((t == null) ? 0 : t.hashCode());
    }
}