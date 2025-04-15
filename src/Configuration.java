import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Logger;


public class Configuration implements Serializable {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private static final Logger logger = Logger.getLogger(Configuration.class.getName());

    public Configuration(){

    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public void serializer(){
        try {
            FileOutputStream fileout = new FileOutputStream("Config.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileout);
            out.writeObject(this);

            logger.info("Configuration object Serialized successfully");
            out.close();
            fileout.close();
        }
        catch (IOException e ){
            logger.severe("Error during serialization");
            e.printStackTrace();
        }
    }
}



