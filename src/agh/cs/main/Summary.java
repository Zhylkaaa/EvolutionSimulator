public class Summary {
    private int daysLived;
    private int descendantsCount;
    private int energy;

    public Summary(int daysLived, int descendantsCount, int energy) {
        this.daysLived = daysLived;
        this.descendantsCount = descendantsCount;
        this.energy = energy;
    }

    public int getDaysLived() {
        return daysLived;
    }

    public int getDescendantsCount() {
        return descendantsCount;
    }

    public int getEnergy() {
        return energy;
    }
}
