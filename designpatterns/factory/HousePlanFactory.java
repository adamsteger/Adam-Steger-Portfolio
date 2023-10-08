package factory;

/**
 * A factory that builds house plans.
 * @author Adam Steger
 */
public class HousePlanFactory {
    
    /**
     * Creates a house plan of a specific type (log, tiny, or contemporary)
     * @param type The type of house being created
     * @return Returns the house plan of the correct house type
     */
    public static HousePlan createHousePlan(String type) {
        if(type.equalsIgnoreCase("log cabin")) {
            HousePlan logCabin = new LogCabinPlan();
            return logCabin;
        } else if(type.equalsIgnoreCase("tiny home")) {
            HousePlan tinyHome = new TinyHomePlan();
            return tinyHome;
        } else if(type.equalsIgnoreCase("contemporary home")) {
            HousePlan contHome = new ContemporaryPlan();
            return contHome;
        } else {
            return null;
        }
    }
}