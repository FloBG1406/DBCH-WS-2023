package de.oth.merkle;

public class MediaWithRatingNode extends MerkleNode {

    private String name;

    private int rating;

    public MediaWithRatingNode(String name, int rating) {
        setName(name);
        setRating(rating);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateHash();
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
        updateHash();
    }

    @Override
    protected String getDataAsString() {
        return getName() + getRating();
    }
}
