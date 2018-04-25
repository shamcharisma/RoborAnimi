package virtual.blockchain.bsystem;

import org.junit.Test;

import java.util.Date;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertTrue;

public class BlockBuilderTest {
    @Test
    public void test_unique_hash_blocks_get_generated() throws ExecutionException, InterruptedException {
        Block genesisBlock = BlockBuilder.createBlock("Welcome to the very first block in Robor Animi verse!", "0");
        System.out.println("Hash for block 1 : " + genesisBlock.getCurrentBlockHash());

        Block secondBlock = BlockBuilder.createBlock("This is the second block in Robor Animi verse", genesisBlock.getCurrentBlockHash());
        System.out.println("Hash for block 2 : " + secondBlock.getCurrentBlockHash());

        Block thirdBlock = BlockBuilder.createBlock("This is the third block", secondBlock.getCurrentBlockHash());
        System.out.println("Hash for block 3 : " + thirdBlock.getCurrentBlockHash());

        assertTrue(!Objects.equals(genesisBlock, secondBlock));
        assertTrue(!Objects.equals(secondBlock, thirdBlock));
        assertTrue(!Objects.equals(genesisBlock, thirdBlock));
    }


    @Test
    public void test_unique_hash_blocks_get_generated_with_same_message() throws ExecutionException, InterruptedException {
        String sameMessage = "Welcome to the very first block in Robor Animi verse!";
        Block genesisBlock = BlockBuilder.createBlock(sameMessage, "0");
        System.out.println("Hash for block 1 : " + genesisBlock.getCurrentBlockHash());

        Block secondBlock = BlockBuilder.createBlock(sameMessage, genesisBlock.getCurrentBlockHash());
        System.out.println("Hash for block 2 : " + secondBlock.getCurrentBlockHash());

        Block thirdBlock = BlockBuilder.createBlock(sameMessage, secondBlock.getCurrentBlockHash());
        System.out.println("Hash for block 3 : " + thirdBlock.getCurrentBlockHash());

        assertTrue(!Objects.equals(genesisBlock, secondBlock));
        assertTrue(!Objects.equals(secondBlock, thirdBlock));
        assertTrue(!Objects.equals(genesisBlock, thirdBlock));
    }

    @Test
    public void test_unique_hash_blocks_get_generated_with_same_message_and_same_previous_block_hash() throws ExecutionException, InterruptedException {
        String sameMessage = "Welcome to the very first block in Robor Animi verse!";
        Block genesisBlock = BlockBuilder.createBlock(sameMessage, "0");
        System.out.println("Hash for block 1 : " + genesisBlock.getCurrentBlockHash());

        Block secondBlock = BlockBuilder.createBlock(sameMessage, "0");
        System.out.println("Hash for block 2 : " + secondBlock.getCurrentBlockHash());

        Block thirdBlock = BlockBuilder.createBlock(sameMessage, "0");
        System.out.println("Hash for block 3 : " + thirdBlock.getCurrentBlockHash());

        assertTrue(!Objects.equals(genesisBlock, secondBlock));
        assertTrue(!Objects.equals(secondBlock, thirdBlock));
        assertTrue(!Objects.equals(genesisBlock, thirdBlock));
    }

    @Test
    public void test_unique_hash_blocks_get_generated_with_same_message_and_same_previous_block_hash_and_same_timestamp() throws ExecutionException, InterruptedException {
        String sameMessage = "Welcome to the very first block in Robor Animi verse!";
        when(BlockBuilder.createBlock(sameMessage, "0").getTimestamp()).thenReturn(new Date().getTime());

        Block genesisBlock = BlockBuilder.createBlock(sameMessage, "0");
        System.out.println("Hash for block 1 : " + genesisBlock.getCurrentBlockHash());

        Block secondBlock = BlockBuilder.createBlock(sameMessage, "0");
        System.out.println("Hash for block 2 : " + secondBlock.getCurrentBlockHash());

        Block thirdBlock = BlockBuilder.createBlock(sameMessage, "0");
        System.out.println("Hash for block 3 : " + thirdBlock.getCurrentBlockHash());

        assertTrue(!Objects.equals(genesisBlock, secondBlock));
        assertTrue(!Objects.equals(secondBlock, thirdBlock));
        assertTrue(!Objects.equals(genesisBlock, thirdBlock));
    }
}