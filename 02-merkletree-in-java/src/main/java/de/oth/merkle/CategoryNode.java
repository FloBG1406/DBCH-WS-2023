package de.oth.merkle;

public class CategoryNode extends MerkleNode {

    private String name;

    public CategoryNode(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateHash();
    }

    @Override
    protected String getDataAsString() {
        return name;
    }
}
