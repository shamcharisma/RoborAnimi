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
        return verifyChainValidity();
    }

    private boolean verifyChainValidity() {
        final LinkedList<Block> blockchain = this.blockchain;

        ListIterator<Block> li = blockchain.listIterator();
        while (li.hasNext()) {
            Block currentBlock = li.next();
            System.out.println(currentBlock.getMessage());

            if (li.hasPrevious()) {
                Block previousBlock = li.previous();
                if (!verifyBlockValidity(currentBlock, previousBlock)) {
                    return false;
                }

                //Set it back to current node, since we iterated to previous node in earlier step
                li.next();
            }
        }
        return true;
    }

    private boolean verifyBlockValidity(Block currentBlock, Block previousBlock) {
        return checkIfExistingBlockHashAndGeneratedBlockHashAreEqual(currentBlock) &&
                checkIfExistingBlockHashAndGeneratedBlockHashAreEqual(previousBlock);
    }

    private boolean checkIfExistingBlockHashAndGeneratedBlockHashAreEqual(Block block) {
        return block.getCurrentBlockHash().equals(generateBlockHash(block));
    }

    private String generateBlockHash(Block block) {
        try {
            return BlockBuilder.generateCurrentBlockHash(block.getMessage(), block.getPreviousBlockHash(), block.getTimestamp());
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
