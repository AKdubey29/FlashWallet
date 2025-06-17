// Transaction.java
public class Transaction {
    private String fromId;
    private String toId;
    private double amount;

    public Transaction(String fromId, String toId, double amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return fromId + "," + toId + "," + amount;
    }

    public static Transaction fromString(String line) {
        String[] parts = line.split(",");
        return new Transaction(parts[0], parts[1], Double.parseDouble(parts[2]));
    }

    public String getFromId() { return fromId; }
    public String getToId() { return toId; }
    public double getAmount() { return amount; }
}


