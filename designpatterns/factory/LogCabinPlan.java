package factory;

/**
 * A type of house plan for a log cabin
 * @author Adam Steger
 */
public class LogCabinPlan extends HousePlan {
    /**
     * Constructs a log cabin plan and sets the variables to default values
     */
    public LogCabinPlan() {
        super(2, 10, 1800);
    }

    /**
     * Adds the materials for a log cabin to its material array list 
     */
    protected void setMaterials() {
        this.materials.add("Log Siding");
        this.materials.add("Board and Batten Siding");
        this.materials.add("White Pine");
    }

    /**
     * Adds the features for a log cabin to its feature list
     */
    protected void setFeatures() {
        this.features.add("Timbered Roof");
        this.features.add("High Insuluation");
        this.features.add("Rustic Effect");
    }

    /**
     * Converts the plan to a string representation
     */
    public String toString() {
        return "Log Cabin\n" + super.toString();
    }
}
