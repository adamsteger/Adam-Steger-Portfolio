package strategy;

import java.util.ArrayList;

/**
 * A way of searching through the list named LinearSearch
 * @author Adam Steger
 */
public class LinearSearch implements SearchBehavior {
    
    /**
     * Searches through a list for a specific item using simple iteration
     * @param data An ArrayList of strings that represents the list of guests
     * @param item The string that is being searched for in the list
     * @return A boolean that is true if the person is found in the list and false if they are not found
     */
    public boolean contains(ArrayList<String> data, String item) {
        for(int i = 0; i < data.size(); i++) {
            if(data.get(i).equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }
}