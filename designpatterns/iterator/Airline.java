package iterator;

/**
 * An airline that tracks a list of flights
 * @author Adam Steger
 */
public class Airline {
    private String title;
    private Flight[] flights;
    private int size;

    /**
     * Creates an airline and initializes its array of flights
     * @param title A string of the title of the airline
     */
    public Airline(String title) {
        this.title = title;
        this.flights = new Flight[2];
        this.size = 0;
    }

    /**
     * Adds a new flight to the airline's flight list
     * @param flightNum A string of the flight number of the flight being added
     * @param from A string of where the added flight is coming from
     * @param to A string of where the added flight is going
     * @param duration An integer of how long the flight will last in minutes
     * @param transfers An integer of how many flight transfers there will be on the added flight
     */
    public void addFlight(String flightNum, String from, String to, int duration, int transfers) {
        Flight flight = new Flight(flightNum, from, to, duration, transfers);
        boolean full = true;
        for(int i = 0; i < flights.length; i++) {
            if(flights[i] == null) {
                full = false;
            }
        }
        if(!full) {
            flights[size] = flight;
            size++;
        } else {
            flights = this.growArray(flights);
            flights[size] = flight;
            size++;
        }
    }

    /**
     * Accesses the title of the airline
     * @return Returns a string of the title of the airline
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Doubles the flight array size
     * @param flights The array of flights that needs to be doubled
     * @return Returns a new flight array of double the size with all the flights from the given array
     */
    private Flight[] growArray(Flight[] flights) {
        Flight[] ret = new Flight[flights.length*2];
        for(int i = 0; i < flights.length; i++) {
           ret[i] = flights[i];
        }
        return ret;
    }

    /**
     * Creates a flight iterator to iterate through the flight array
     * @return Returns a flight iterator to iterate through the airline's flight array
     */
    public FlightIterator createIterator() {
        FlightIterator iterator = new FlightIterator(flights);
        return iterator;
    }
    
}
