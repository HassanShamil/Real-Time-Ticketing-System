import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Vector;

class TicketPool {

    private Vector<Ticket> availableTickets;
    private final Lock lock = new ReentrantLock();  // Lock to ensure thread-safety
    private int nextTicketId = 1; // Tracking the next unique ticket ID
    private int maxCapacity;
    private static int soldTickets = 0;

    public TicketPool(int initialTickets) {
        availableTickets = new Vector<>();
        availableTickets.setSize(this.maxCapacity); // setting the size of the vector
        for (int i = 0; i < initialTickets; i++) {
            availableTickets.add(new Ticket(nextTicketId++)); // Initialize pool with unique tickets
        }
    }

    public Vector<Ticket> getAvailableTickets() {
        return availableTickets;
    }

    public static int getSoldTickets() {
        return soldTickets;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    // thread safe method to release tickets into the pool
    public void releaseTicket(int numTickets) {
        lock.lock();  // Acquire lock
        try {
            if (availableTickets.size() + numTickets < this.maxCapacity){
                for (int i = 0; i < numTickets; i++) {
                    availableTickets.add(new Ticket(nextTicketId++)); // Add new tickets with unique IDs
                }

            }
            else {
                System.err.println("Ticket pool Reached max capacity");
            }

        } finally {
            lock.unlock();  // Release lock
        }
    }


    // Method to purchase tickets from the pool (Thread-safe)
    public Vector<Ticket> purchaseTicket(int numTickets) {
        lock.lock();
        try {
            if (numTickets <= availableTickets.size()) {
                Vector<Ticket> purchasedTickets = new Vector<>();
                for (int i = 0; i < numTickets; i++) {
                    purchasedTickets.add(availableTickets.remove(0)); // Remove tickets from the pool
                }
                soldTickets+= purchasedTickets.size();

                return purchasedTickets;
            } else {
                System.err.println("Not enough tickets to purchase " + numTickets + ". Available tickets: " + availableTickets.size());

                return new Vector<>();
            }
        } finally {
            lock.unlock();
        }
    }
}