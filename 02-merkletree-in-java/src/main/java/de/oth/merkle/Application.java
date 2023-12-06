package de.oth.merkle;

public class Application {

    public static void main(String[] args) {

        MerkleNode romaneNode = createCategorie("Romane", "Julia ist cool", "Max in Gefahr");
        MerkleNode sachbuchNode = createCategorie("Sachbuch", "Java ist auch eine Insel", "JavaScript ist auch ein Berg");
        MerkleNode krimiNode = createCategorie("Krimi", "Der Mörder ist der Gärtner", "Der Mörder ist der Koch");
        MerkleNode fantasyNode = createCategorie("Fantasy", "Der Herr der Ringe", "Harry Potter");

        MerkleNode buecherNode = new CategoryNode("Bücher");
        buecherNode.addChild(romaneNode);
        buecherNode.addChild(sachbuchNode);
        buecherNode.addChild(krimiNode);
        buecherNode.addChild(fantasyNode);

        MerkleNode actionFilmeNode = createCategorie("Action", "Terminator", "Rambo");
        MerkleNode horrorFilmeNode = createCategorie("Horror", "Saw", "Halloween");
        MerkleNode dramaFilmeNode = createCategorie("Drama", "Titanic", "Der Pate");
        MerkleNode comedyFilmeNode = createCategorie("Comedy", "Hangover", "American Pie");
        MerkleNode filmeNode = new CategoryNode("Filme");
        filmeNode.addChild(actionFilmeNode);
        filmeNode.addChild(horrorFilmeNode);
        filmeNode.addChild(dramaFilmeNode);
        filmeNode.addChild(comedyFilmeNode);

        MerkleNode rootNode = new CategoryNode("Medien");
        rootNode.addChild(buecherNode);
        rootNode.addChild(filmeNode);


        final MediaWithRatingNode node = findMedia(rootNode, "Titanic");
        node.setRating(5);

        final String hashOfMyMerkeTree = rootNode.getHash();
    }

    private static MediaWithRatingNode findMedia(MerkleNode root, String mediaName) {
        if(root instanceof MediaWithRatingNode mediaNode) {
            if(mediaNode.getName().equals(mediaName)) {
                return mediaNode;
            }
        }
        for(MerkleNode child : root.getChildren()) {
            MediaWithRatingNode mediaNode = findMedia(child, mediaName);
            if(mediaNode != null) {
                return mediaNode;
            }
        }
        return null;
    }

    private static MerkleNode createCategorie(String name, String... data) {
        MerkleNode categorieNode = new CategoryNode(name);
        for(String d : data) {
            categorieNode.addChild(new MediaWithRatingNode(d, 0));
        }
        return categorieNode;
    }
}
