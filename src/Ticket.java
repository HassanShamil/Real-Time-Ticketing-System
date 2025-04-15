class Ticket {
    private final int ticketId;
    private Vendor vendor;
    private Customer customer;
    private static int ticketCount = 0;

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Ticket(int ticketId) {
        this.ticketId = ticketId;
        ticketCount++;
    }

    public static int getTicketCount() {
        return ticketCount;
    }

    @Override
    public String toString() {
        return "Ticket#" + ticketId;
    }
}
