package factory;

/**
 * A type of house plan for a tiny home
 * @author Adam Steger
 */
public class TinyHomePlan extends HousePlan {
    /**
     * Creates a tiny home plan and sets the variables to default values
     */
    public TinyHomePlan() {
        super(1, 5, 200);
    }

    /**
     * Adds the materials for a tiny home to its materials list
     */
    protected void setMaterials() {
        this.materials.add("Lumber");
        this.materials.add("Insulation");
        this.materials.add("Metal Roofing");
        this.materials.add("Hardware");
    }

    /**
     * Adds the features for a tiny home to its features list
     */
    protected void setFeatures() {
        this.features.add("Natural Light");
        this.features.add("Creative Storage");
        this.features.add("Multipurpose Areas");
        this.features.add("Multi-use Applications");
    }

    /**
     * Converts the tiny home plan to a string representation
     */
    public String toString() {
        return "Tiny House\n" + super.toString();
    }
}
