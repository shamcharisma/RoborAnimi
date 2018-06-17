package virtual.blockchain.bsystem;

import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Log4j2
public class RayChain {
    private final LinkedList<Block> blockchain;

    private RayChain(LinkedList<Block> blockchain) {
        this.blockchain = blockchain;
    }

    public static RayChain createRayChain(LinkedList<Block> blockchain) {
        return new RayChain(blockchain);
    }

    public boolean isRayChainValid() {
        return isValidRayChain();
    }

    private boolean isValidRayChain() {
        final LinkedList<Block> blockchain = this.blockchain;

        ListIterator<Block> li = blockchain.listIterator();
        while (li.hasNext()) {
            Block currentBlock = blockchain.get(li.nextIndex());

            if (li.previousIndex() >= 0) {

                Block previousBlock = blockchain.get(li.previousIndex());
                if (!isValidBlock(currentBlock, previousBlock)) {
                    return false;
                }
            }
            li.next();
        }
        return true;
    }

    private boolean isValidBlock(Block currentBlock, Block previousBlock) {
        return checkIfCurrentBlockHashAndGeneratedBlockHashAreEqual(currentBlock) &&
                checkIfPreviousBlockHashAndGeneratedBlockHashAreEqual(currentBlock, previousBlock) &&
                comparePreviousBlockHash(currentBlock, previousBlock);
    }

    private boolean checkIfCurrentBlockHashAndGeneratedBlockHashAreEqual(Block block) {
        return block.getCurrentBlockHash().equals(generateBlockHash(block));
    }

    private boolean comparePreviousBlockHash(Block currentBlock, Block previousBlock) {
        return currentBlock.getPreviousBlockHash().equals(previousBlock.getCurrentBlockHash());
    }

    private boolean checkIfPreviousBlockHashAndGeneratedBlockHashAreEqual(Block currentBlock, Block previousBlock) {
        return currentBlock.getPreviousBlockHash().equals(generateBlockHash(previousBlock));
    }

    private String generateBlockHash(Block block) {
        try {
            return BlockBuilder.generateCurrentBlockHash(block);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error generating current cryptographic Block hash: {}", e);
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            log.error("Timeout during generation of current cryptographic Block hash: {}", e);
            throw new RuntimeException(e);
        }
    }

    private Optional<Block> getRayChainLastBlock() {
        return Optional.of(blockchain.stream()
                .reduce((a, b) -> b)
                .orElseThrow(() -> new IllegalStateException("Illegal operation detected: no last element in Robor Animi chain")));
    }
}
