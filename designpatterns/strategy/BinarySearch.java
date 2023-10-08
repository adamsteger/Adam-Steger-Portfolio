package strategy;

import java.util.*;

/**
 * A type of searching through the list named BinarySearch
 * @author Adam Steger
 */
public class BinarySearch implements SearchBehavior {
    private int low;
    private int high;
    private int mid;

    /**
     * Searches through the list using binary search
     * @param data An ArrayList of strings that represents the list of guests
     * @param item The string that is being searched for in the list
     * @return A boolean that is true if the person is found in the list and false if they are not found
     * I referenced this source when writing this code: https://www.geeksforgeeks.org/binary-search-a-string/
     */
    public boolean contains(ArrayList<String> data, String item) {
        Collections.sort(data);
        low = 0;
        high = data.size() - 1;
        while(low <= high) {
            mid = (low + high) / 2;
            if(item.compareTo(data.get(mid)) == 0) {
                return true;
            }
            else if(item.compareTo(data.get(mid)) > 0) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return false;
    }
}