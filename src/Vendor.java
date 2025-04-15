import java.util.ArrayList;

class Vendor extends Thread {
    private final String name;
    private final TicketPool ticketPool;
    private int fixedRelease;  // Fixed number of tickets to release per cycle
    private final long endTime;
    private static int vendorCount = 0;
    private static ArrayList<Vendor> Vendors = new ArrayList<>();
    private static long vendorCoolingTime;

    public Vendor(String name, TicketPool ticketPool, long simulationTime) {
        this.name = name;
        this.ticketPool = ticketPool;
        this.endTime =  simulationTime;  // Set end time for the simulation
        vendorCount++;
        Vendors.add(this);
    }

    public static void setVendorCoolingTime(long vendorCoolingTime) {
        Vendor.vendorCoolingTime = vendorCoolingTime;
    }

    public void setFixedRelease(int fixedRelease) {
        this.fixedRelease = fixedRelease;
    }

    public static int getVendorCount() {
        return vendorCount;
    }

    public static ArrayList<Vendor> getVendors() {
        return Vendors;
    }


    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < endTime + startTime ) {
            if (fixedRelease > 0) {
                ticketPool.releaseTicket(fixedRelease);      // Release fixed number of tickets
                System.out.println(name + " releasing " + fixedRelease + " ticket. \nAvailable tickets: " + ticketPool.getAvailableTickets().size());

                try {
                    Thread.sleep(vendorCoolingTime);  // Sleep for a fixed time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.out.println(name + " finished releasing tickets.");
    }

    // getting vendor details
    public static void vendorDetails(){
        System.out.println("Total Vendors: "+vendorCount);
        System.out.println("Vendor cooling time: "+vendorCoolingTime);
        for (int i = 0; i < vendorCount; i++){
            System.out.println(getVendors().get(i).name+" : releases "+getVendors().get(i).fixedRelease+" tickets" );
         }
    }
}


