package factory;

import java.util.ArrayList;

/**
 * A house plan that stores information about the house
 * @author Adam Steger
 */
public abstract class HousePlan {
    protected ArrayList<String> materials;
    protected ArrayList<String> features;
    private int numRooms;
    private int numWindows;
    private int squareFeet;

    /**
     * Creates a house plan and its materials and features
     * @param numRooms an integer representation of the number of rooms in the house
     * @param numWindows an integer representation of the number of windows in the house
     * @param squareFeet an integer representation of the square feet of the house
     */
    public HousePlan(int numRooms, int numWindows, int squareFeet) {
        this.numRooms = numRooms;
        this.numWindows = numWindows;
        this.squareFeet = squareFeet;
        this.materials = new ArrayList<String>();
        this.features = new ArrayList<String>();
        setMaterials();
        setFeatures();
    }

    /**
     * Adds the materials to the materials arraylist
     */
    protected abstract void setMaterials();
    
    /**
     * Adds the features to the features arraylist
     */
    protected abstract void setFeatures();

    /**
     * Accesses the materials arraylist
     * @return Returns the arraylist of materials
     */
    public ArrayList<String> getMaterials() {
        return this.materials;
    }

    /**
     * Accesses the features arraylist
     * @return Returns the arraylist of features
     */
    public ArrayList<String> getFeatures() {
        return this.features;
    }

    /**
     * Accesses the number of rooms of the house
     * @return Returns an integer representation of the number of rooms
     */
    public int getNumRooms() {
        return this.numRooms;
    }

    /**
     * Accesses the number of windows of the house
     * @return Returns an integer representation of the number of windows
     */
    public int getNumWindows() {
        return this.numWindows;
    }

    /**
     * Accesses the square feet of the house
     * @return Returns an integer representation of the number of square feet
     */
    public int getSquareFeet() {
        return this.squareFeet;
    }

    /**
     * Converts the house plan to a string
     * @return Returns the string representation of a house plan
     */
    public String toString() {
        String materialString = "";
        for(String s : this.materials) {
            materialString = materialString + " - " + s + "\n";
        }
        String featureString = "";
        for(String s : this.features) {
            featureString = featureString + " - " + s + '\n';
        }
        return ("Square Feet: " + this.squareFeet + "\nRoom: " + 
                    this.numRooms + "\nWindows: " + this.numWindows + 
                    "\n\nMaterials: \n" + materialString + "\nFeatures: \n"
                    + featureString);
    }
}