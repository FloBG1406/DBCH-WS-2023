package com.openelements.dbch;

import java.time.LocalDateTime;
import java.util.Objects;

public record Block(LocalDateTime timestamp, String data, String previousHash, boolean genesisBlock) {

    public Block {
        Objects.requireNonNull(timestamp);
        if(timestamp.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Timestamp cannot be in the future");
        }
        Objects.requireNonNull(data);
        if(!genesisBlock) {
            Objects.requireNonNull(previousHash);
        }
    }

    public Block(String data, String previousHash) {
        this(LocalDateTime.now(), data, previousHash, false);
    }

    public Block(String data) {
        this(LocalDateTime.now(), data, null, true);
    }
}
