import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;

class Customer extends Thread {

    private final String name;
    private final TicketPool ticketPool;
    private int fixedPurchase = 0;  // Fixed number of tickets to attempt to purchase per cycle
    private final long endTime;
    private static int customerCount = 0;
    private static ArrayList<Customer> Customers = new ArrayList<>();
    private static long  customerCoolingTime;
    private static final Logger logger = Logger.getLogger(Customer.class.getName());

    public Customer(String name, TicketPool ticketPool, long simulationTime) {
        this.name = name;
        this.ticketPool = ticketPool;
        this.endTime =  simulationTime;  // Set end time for the simulation
        customerCount++;
        Customers.add(this);
    }

    public static ArrayList<Customer> getCustomers() {
        return Customers;
    }


    public void setFixedPurchase(int fixedPurchase) {
        this.fixedPurchase = fixedPurchase;
    }

    public static int getCustomerCount() {
        return customerCount;
    }


    public static void setCustomerCoolingTime(long customerCoolingTime) {
        Customer.customerCoolingTime = customerCoolingTime;
    }


    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < endTime + startTime) {

            Vector<Ticket> purchasedTickets = ticketPool.purchaseTicket(fixedPurchase);  // Try to purchase tickets
            if (!purchasedTickets.isEmpty()) {
                logger.info(name+" purchased "+purchasedTickets);
                System.out.println(name + " successfully purchased " + purchasedTickets + "\nAvailable tickets : "+ticketPool.getAvailableTickets().size());
            }
            try {
                Thread.sleep(customerCoolingTime);  // Sleep for a fixed time (1.5 seconds)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // getting customer details
    public static void customerDetails(){
        System.out.println("Total Customers: "+customerCount+"\nCustomer Cooling Time: "+customerCoolingTime);
        for (int i = 0; i < customerCount; i++){
            System.out.println(getCustomers().get(i).name+" : purchases "+getCustomers().get(i).fixedPurchase+" tickets" );
        }
    }
}