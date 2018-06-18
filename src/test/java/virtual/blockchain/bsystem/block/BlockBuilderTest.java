package virtual.blockchain.bsystem.block;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import virtual.blockchain.bsystem.block.Block;
import virtual.blockchain.bsystem.block.BlockBuilder;

import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertTrue;

public class BlockBuilderTest {
    private AtomicInteger nonce = new AtomicInteger(1);

    @Test
    public void test_unique_hash_blocks_get_generated() {
        Block genesisBlock = BlockBuilder.createBlock("Welcome to the very first block in Robor Animi verse!", "0", nonce);
        Block secondBlock = BlockBuilder.createBlock("This is the second block in Robor Animi verse", genesisBlock.getCurrentBlockHash(), nonce);
        Block thirdBlock = BlockBuilder.createBlock("This is the third block", secondBlock.getCurrentBlockHash(), nonce);

        verifyIfUniqueBlocks(genesisBlock, secondBlock, thirdBlock);
    }

    @Test
    public void test_unique_hash_blocks_get_generated_with_same_message() {
        String sameMessage = "Welcome to the very first block in Robor Animi verse!";

        Block genesisBlock = BlockBuilder.createBlock(sameMessage, "0", nonce);
        Block secondBlock = BlockBuilder.createBlock(sameMessage, genesisBlock.getCurrentBlockHash(), nonce);
        Block thirdBlock = BlockBuilder.createBlock(sameMessage, secondBlock.getCurrentBlockHash(), nonce);

        verifyIfUniqueBlocks(genesisBlock, secondBlock, thirdBlock);
    }

    @Ignore
    public void test_unique_hash_blocks_get_generated_with_same_message_and_same_previous_block_hash() {
        String sameMessage = "Welcome to the very first block in Robor Animi verse!";
        Block genesisBlock = BlockBuilder.createBlock(sameMessage, "0", nonce);
        Block secondBlock = BlockBuilder.createBlock(sameMessage, "0", nonce);
        Block thirdBlock = BlockBuilder.createBlock(sameMessage, "0", nonce);

        verifyIfUniqueBlocks(genesisBlock, secondBlock, thirdBlock);
    }

   /* @Ignore
    public void test_unique_hash_blocks_get_generated_with_same_message_and_same_previous_block_hash_and_same_timestamp() throws ExecutionException, InterruptedException {
        String sameMessage = "Welcome to the very first block in Robor Animi verse!";
        when(BlockBuilder.createBlock(anyString(), anyString())).thenReturn(BlockBuilder.createBlock(sameMessage, "test", nonce));
        when(block.getTimestamp()).thenReturn(new Date().getTime());

        Block genesisBlock = BlockBuilder.createBlock(sameMessage, "0");
        Block secondBlock = BlockBuilder.createBlock(sameMessage, "0");
        Block thirdBlock = BlockBuilder.createBlock(sameMessage, "0");

        verifyIfUniqueBlocks(genesisBlock, secondBlock, thirdBlock);
    }*/

    private void verifyIfUniqueBlocks(Block genesisBlock, Block secondBlock, Block thirdBlock) {
        System.out.println("Genesis: " + genesisBlock.getCurrentBlockHash());
        System.out.println("Second: " + secondBlock.getCurrentBlockHash());
        System.out.println("Third: " + thirdBlock.getCurrentBlockHash());

        assertTrue(!genesisBlock.equals(secondBlock));
        assertTrue(!secondBlock.equals(thirdBlock));
        assertTrue(!thirdBlock.equals(genesisBlock));
    }
}