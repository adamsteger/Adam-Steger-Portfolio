package factory;

/**
 * A type of house plan for a contemporary home
 * @author Adam Steger
 */
public class ContemporaryPlan extends HousePlan {
    
    /**
     * Constructs the contemporary home plan and sets the variables to default values
     */
    public ContemporaryPlan() {
        super(5, 40, 3000);
    }

    /**
     * Adds the materials for a contemporary home to its materials list
     */
    protected void setMaterials() {
        this.materials.add("Ceramics");
        this.materials.add("High-Strength Alloys");
        this.materials.add("Composites");
    }

    /**
     * Adds the features for a contemporary home to its features list
     */
    protected void setFeatures() {
        this.features.add("Oversized Windows");
        this.features.add("Unconventional Roof");
        this.features.add("Minimalism");
        this.features.add("Open Floor Plan");
    }

    /**
     * Converts the contemporary home plan to a string representation
     */
    public String toString() {
        return "Contemporary Home\n" + super.toString();
    }
}
