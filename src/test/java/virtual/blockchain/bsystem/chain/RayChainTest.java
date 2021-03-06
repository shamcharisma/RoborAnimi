package virtual.blockchain.bsystem.chain;

import org.junit.Test;
import virtual.blockchain.bsystem.block.Block;
import virtual.blockchain.bsystem.block.BlockBuilder;
import virtual.blockchain.bsystem.chain.RayChain;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.TestCase.*;


public class RayChainTest {
    private AtomicInteger nonce = new AtomicInteger(1);

    @Test
    public void check_if_valid_ray_chain_is_accepted() {
        final LinkedList<Block> blockchain = new LinkedList<>();

        Block genesisBlock = BlockBuilder.createBlock("Welcome to the very first block in Robor Animi verse!", "0", nonce);
        blockchain.add(genesisBlock);

        Block secondBlock = BlockBuilder.createBlock("This is the second block in Robor Animi verse", genesisBlock.getCurrentBlockHash(), nonce);
        blockchain.add(secondBlock);

        Block thirdBlock = BlockBuilder.createBlock("This is the third block", secondBlock.getCurrentBlockHash(), nonce);
        blockchain.add(thirdBlock);

        final RayChain rayChain = RayChain.createRayChain(blockchain);
        assertTrue(rayChain.isRayChainValid());
    }

    @Test
    public void check_if_invalid_ray_chain_is_rejected() {
        final LinkedList<Block> blockchain = new LinkedList<>();

        Block genesisBlock = BlockBuilder.createBlock("Welcome to the very first block in Robor Animi verse!", "0", nonce);
        blockchain.add(genesisBlock);

        Block secondBlock = BlockBuilder.createBlock("This is the second block in Robor Animi verse", "different hash", nonce);
        blockchain.add(secondBlock);

        Block thirdBlock = BlockBuilder.createBlock("This is the third block", secondBlock.getCurrentBlockHash(), nonce);
        blockchain.add(thirdBlock);

        final RayChain rayChain = RayChain.createRayChain(blockchain);
        assertFalse(rayChain.isRayChainValid());
    }
}
