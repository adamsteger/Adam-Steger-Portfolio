package strategy;

import java.util.ArrayList;

/**
 * An interface for the types of search behaviors that requires a contains method
 * @author Adam Steger
 */
public interface SearchBehavior {

    /**
     * Searches through the list of guests for a specific person
     * @param data An ArrayList of strings that represents the list of guests
     * @param item The string that is being searched for in the list
     * @return A boolean that is true if the person is found in the list and false if they are not found
     */
    public boolean contains(ArrayList<String> data, String item);
}