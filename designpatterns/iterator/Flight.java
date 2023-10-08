package iterator;

/**
 * A flight from one place to another
 * @author Adam Steger
 */
public class Flight {
    private String flightNum;
    private String from;
    private String to;
    private int duration;
    private int transfers;

    /**
     * Creates a flight and initializes its values
     * @param flightNum A string of the flight number of the flight
     * @param from A string of the place the flight is leaving
     * @param to A string of the destination of the flight
     * @param duration An integer of the length of the flight in minutes
     * @param transfers An integer of the number of transfers on the flight
     */
    public Flight(String flightNum, String from, String to, int duration, int transfers) {
        this.flightNum = flightNum;
        this.from = from;
        this.to = to;
        this.duration = duration;
        this.transfers = transfers;
    }

    /**
     * Accesses where the flight is coming from
     * @return Returns a string of where the flight is coming from
     */
    public String getFrom() {
        return this.from;
    }

    /**
     * Accesses the final destination of the flight
     * @return Returns a string of the final destination of the flight
     */
    public String getTo() {
        return this.to;
    }

    /**
     * Acceses the duration of the flight
     * @return Returns an integer of how long the flight will last in minutes
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Acceses the number of transfers on the flight
     * @return Returns an integer of the number of transfers on the flight
     */
    public int getNumTransfers() {
        return this.transfers;
    }

    /**
     * Converts the flight to a formatted string
     * @return Returns a string representation of the flight
     */
    public String toString() {
        String durationString;
        int durationHours, durationMinutes;
        durationHours = duration / 60;
        durationMinutes = duration % 60;
        durationString = durationHours + " hours " + durationMinutes + " minutes";
        String transferString;
        if(this.transfers == 0) {
            transferString = "Direct Flight";
        } else if(this.transfers == 1) {
            transferString = "1 Transfer";
        } else {
            transferString = this.transfers + " Transfers";
        }
        return "Flight Number: " + this.flightNum + "\nFrom: " + this.from + "\nTo: " +
                    this.to + "\nDuration: " + durationString + "\n" + transferString;
    }
}
