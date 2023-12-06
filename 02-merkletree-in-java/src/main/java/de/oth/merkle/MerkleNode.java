package de.oth.merkle;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public abstract class MerkleNode {

    private MerkleNode parent;

    private String hash;

    private List<MerkleNode> children;

    public MerkleNode() {
        this.children = new ArrayList<>();
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    protected abstract String getDataAsString();

    protected void updateHash() {
        System.out.println("Updating hash for data '" + getDataAsString() + "'");
        String dataToHash = getDataAsString() + children.stream().map(c -> c.hash).reduce("", (a, b) -> a + b);
        System.out.println("Data to Hash: '" + dataToHash + "'");
        final byte[] message = dataToHash.getBytes(StandardCharsets.UTF_8);
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            final byte[] hash = md.digest(message);
            this.hash = Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Hash algorithm error", e);
        }
        System.out.println("New Hash: '" + hash + "'");
        if(!isRoot()) {
            parent.updateHash();
        }
    }

    public List<MerkleNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void addChild(MerkleNode node) {
        node.setParent(this);
        children.add(node);
    }

    public void removeChild(MerkleNode node) {
        if(!children.contains(node)) {
            throw new IllegalArgumentException("Node is not a child of this node");
        }
        node.setParent(null);
        children.remove(node);
    }

    private void setParent(MerkleNode parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data='" + getDataAsString() + '\'' +
                ", hash='" + hash + '\'' +
                ", children=" + children.stream().map(c -> c.toString() + System.lineSeparator()).reduce("", (a, b) -> a + b) +
                '}';
    }

    public String getHash() {
        return hash;
    }
}
