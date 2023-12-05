package com.openelements.dbch;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Chain {

    public static final String HASH_MINING_PREFIX = "00";
    private List<Block> blocks;

    public Chain() {
        this.blocks = new ArrayList<>();
    }

    private Block mining(Function<Integer, Block> blockCreatoFunction) {
        Random random = new Random(System.currentTimeMillis());
        int nonce = -1;
        Block block = blockCreatoFunction.apply(nonce);
        while (!calculateHash(block).startsWith(HASH_MINING_PREFIX)) {
            nonce = random.nextInt();
            System.out.println("Trying with nonce " + nonce);
            block = blockCreatoFunction.apply(nonce);
        }
        return block;
    }

    public void addBlock(String data) {
        Random random = new Random(System.currentTimeMillis());
        if (blocks.isEmpty()) {
            Block block = mining(nonce -> new Block(data, nonce));
            blocks.add(block);
        } else {
            String hashFromPrevBlock = calculateHash(getLastBlock());
            Block block = mining(nonce -> new Block(data, hashFromPrevBlock, nonce));
            blocks.add(block);
         }
    }

    public Block getLastBlock() {
        if(blocks.isEmpty()) {
           throw new IllegalStateException("Chain is empty");
        }
        return blocks.get(blocks.size() - 1);
    }

    public void verify() {
        if(blocks.isEmpty()) {
            return;
        }

        Block genesisBlock = blocks.get(0);
        if(!genesisBlock.genesisBlock()) {
            throw new IllegalStateException("Genesis Block is not the first block");
        }
        if(genesisBlock.previousHash() != null) {
            throw new IllegalStateException("Genesis Block should not have a previous hash");
        }

        for(int i = 1; i < blocks.size(); i++) {
            Block currentBlock = blocks.get(i);
            if(currentBlock.genesisBlock()) {
                throw new IllegalStateException("Genesis Block is not the first block");
            }
            Block previousBlock = blocks.get(i - 1);
            if(currentBlock.timestamp().isBefore(previousBlock.timestamp())) {
                throw new IllegalStateException("Timestamp of block " + i + " is before timestamp of block " + (i - 1));
            }
            if(!currentBlock.previousHash().equals(calculateHash(previousBlock))) {
                throw new IllegalStateException("Hash of block " + i + " is not equal to hash of block " + (i - 1));
            }
        }
    }

    private static String calculateHash(Block block) {
        final String value = block.timestamp() + block.data() + block.previousHash();
        final byte[] message = value.getBytes(StandardCharsets.UTF_8);
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            final byte[] hash = md.digest(message);
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Kann Hash nicht berechnen, da Hash-Alogrithmus nicht gefunden", e);
        }
    }

    @Override
    public String toString() {
        return "Chain{" +
                "blocks=" + blocks +
                '}';
    }
}
