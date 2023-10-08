package iterator;

import java.util.Iterator;
/**
 * A type of iterator that iterates through a flight array
 * @author Adam Steger
 */
public class FlightIterator implements Iterator<Flight> {
    private Flight[] flights;
    private int position;

    /**
     * Creates a flight iterator for a given flight array and initializes the current position to -1
     * @param flights The array of flights that is being iterated over
     */
    public FlightIterator(Flight[] flights) {
        this.flights = flights;
        position = 0;
    }

    /**
     * Checks to see if the next index of the flight array exists
     * @return Returns true if the next index of the flight array is not null and true if it is null
     */
    public boolean hasNext() {
        boolean ret = false;
        if(flights[position] != null) {
            ret = true;
        }
        return ret;
    }

    /**
     * Moves to the next index in the array if it exists
     * @return Returns the flight at the next index of the array
     */
    public Flight next() {
        if(this.hasNext()) {
            position++;
            return flights[position-1];
        } else {
            return null;
        }
    }
}
