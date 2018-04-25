import com.google.gson.GsonBuilder;
import virtual.blockchain.bsystem.Block;
import virtual.blockchain.bsystem.BlockBuilder;
import virtual.blockchain.bsystem.RayChain;

import java.util.LinkedList;

public class main {
    public static void main(String[] args) {
        final LinkedList<Block> blockchain = new LinkedList<>();
        final Block genesisBlock = BlockBuilder.createBlock("Welcome to the very first block in Robor Animi verse!", "0");
        blockchain.add(genesisBlock);

        Block secondBlock = BlockBuilder.createBlock("This is the second block in Robor Animi verse", genesisBlock.getCurrentBlockHash());
        blockchain.add(secondBlock);

        Block thirdBlock = BlockBuilder.createBlock("This is the third block", secondBlock.getCurrentBlockHash());
        blockchain.add(thirdBlock);

        final RayChain rayChain = RayChain.createRayChain(blockchain);
        boolean validRayChain = rayChain.isRayChainValid();

        final String rayChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(rayChain);
        System.out.println(rayChainJson);
        System.out.println("Validated RayChain: " + validRayChain);
    }
}
