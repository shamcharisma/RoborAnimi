import com.google.gson.GsonBuilder;
import virtual.blockchain.bsystem.block.Block;
import virtual.blockchain.bsystem.block.BlockBuilder;
import virtual.blockchain.bsystem.chain.RayChain;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class main {
    public main(String[] args) {
        final LinkedList<Block> blockchain = new LinkedList<>();
        final AtomicInteger nonce = new AtomicInteger(1);

        final Block genesisBlock = BlockBuilder.createBlock("Welcome to the very first block in Robor Animi verse!", "0", nonce);
        blockchain.add(genesisBlock);

        Block secondBlock = BlockBuilder.createBlock("This is the second block in Robor Animi verse", genesisBlock.getCurrentBlockHash(), nonce);
        blockchain.add(secondBlock);

        Block thirdBlock = BlockBuilder.createBlock("This is the third block", secondBlock.getCurrentBlockHash(), nonce);
        blockchain.add(thirdBlock);

        final RayChain rayChain = RayChain.createRayChain(blockchain);
        boolean validRayChain = rayChain.isRayChainValid();

        BlockBuilder.mineBlock(blockchain.get(1));

        final String rayChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(rayChain);
        System.out.println(rayChainJson);
        System.out.println("Validated RayChain: " + validRayChain);
    }
}
