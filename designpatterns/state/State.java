package state;

public interface State {
    /**
     * Accesses a random number within the given range for a level
     * @return Returns a random integer within the given range for a level
     */
    public int getNum();

    /**
     * Accesses a random operation within the given options for a level
     * @return Returns a string with the random operation
     */
    public String getOperation();

    /**
     * Advances the game to the next level/state if possible and communicates it to the user
     */
    public void levelUp();

    /**
     * Decreases the game to the past level/state if possible and communicates it to the user
     */
    public void levelDown();
}
