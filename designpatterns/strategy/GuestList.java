package strategy;

import java.util.ArrayList;

/**
 * A List of Guests at Thanksgiving. 
 * @author Adam Steger
 */
public class GuestList {
    private String title;
    private ArrayList<String> people;
    private SearchBehavior searchBehavior;

    /**
     * Creates instances of the list and the search behavior, as well as set the title of the list
     * @param title A string that is the name of the list
     */
    public GuestList(String title) {
        people = new ArrayList<String>();
        this.title = title;
        this.searchBehavior = new LinearSearch();
    }

    /**
     * Adds a new person to the list if they are not already on the list
     * @param person A string that is the person that needs to be added to the list
     * @return A boolean that is false if the person is already in the list and true if the person is added to the list
     */
    public boolean add(String person) {
        if(searchBehavior.contains(people, person)) {
            return false;
        }
        else {
            people.add(person);
            return true;
        }
    }

    /**
     * Removes a person from the list if they are on the list
     * @param person A string that is the person that needs to be removed
     * @return A boolean that is true if the person is removed and false if they are not on the list
     */
    public boolean remove(String person) {
        if(searchBehavior.contains(people, person)) {
            people.remove(person);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Accesses the title of the list
     * @return A string that represents the name of the list
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Changes the type of search behavior of the list
     * @param searchBehavior The new search behavior that it is being set to (either LinearSearch or BinarySearch)
     */
    public void setSearchBehavior(SearchBehavior searchBehavior) {
        this.searchBehavior = searchBehavior;
    }

    /**
     * Accesses the title of the list
     * @return A string that is the name of the list
     */
    public ArrayList<String> getList() {
        return this.people;
    }
}