import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

    static final Scanner input = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    static {
        try {
            // Creating a FileHandler to log to a file called "app.log"
            FileHandler fileHandler = new FileHandler("app.log", false);  // Append mode
            fileHandler.setFormatter(new SimpleFormatter());  // Simple log format
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            logger.warning("error");

        }
    }

    public static void main(String[] args) {

        final String totTicketMsg = "Enter the total number of tickets";
        final String relRateMsg = "Enter the ticket release rate/tickets released per cycle)";
        final String retRateMsg = "Enter the customer retrieval rate/tickets retrieved per cycle)";
        final String maxTicketMsg = "Enter the maximum ticket capacity";
        final String cusCoolTimeMsg = "Enter the Customer Cooling time in milliseconds ";
        final String venCoolTimeMsg = "Enter the Vendor Cooling time in milliseconds ";
        final String totCusMsg = "Enter the number of customers";
        final String totVenMsg = "Enter the number of vendors";
        final String simTimeMsg = "Enter the simulation time";

        Configuration config = new Configuration(); // Creating a configuration object

        System.out.println("************************************");
        System.out.println("  Welcome to the Ticketing System");
        System.out.println("************************************\n");

        String user;
        System.out.println("Do you want to load previous configuration details (yes/no)");
        user = input.nextLine();

        while (true) {
            if (user.equalsIgnoreCase("yes")) {
                try (BufferedReader reader = new BufferedReader(new FileReader("configDetails.txt"))) {
                    config.setTotalTickets(Integer.parseInt(reader.readLine().split(":")[1].trim()));
                    config.setTicketReleaseRate(Integer.parseInt(reader.readLine().split(":")[1].trim()));
                    config.setCustomerRetrievalRate(Integer.parseInt(reader.readLine().split(":")[1].trim()));
                    config.setMaxTicketCapacity(Integer.parseInt(reader.readLine().split(":")[1].trim()));
                    System.out.println("Loaded values from file:");
                    System.out.println("Total Tickets: " + config.getTotalTickets());
                    System.out.println("Ticket Release rate: " + config.getTicketReleaseRate());
                    System.out.println("Customer Retrieval rate: " + config.getCustomerRetrievalRate());
                    System.out.println("Max Ticket Capacity: " + config.getMaxTicketCapacity());

                } catch (IOException | NumberFormatException e) {
                    // If file doesn't exist or is empty, prompt user for input
                    System.out.println("No previous data found. Please enter values:");
                    config.setTotalTickets(validateInput(totTicketMsg+"between", 0, 100));
                    config.setTicketReleaseRate(validateInput(relRateMsg, 0, 20));
                    config.setCustomerRetrievalRate(validateInput(retRateMsg, 0, 20));
                    config.setMaxTicketCapacity(validateInput(maxTicketMsg, 100, 1000));
                }
                break;

            } else if (user.equalsIgnoreCase("no")) {

                config.setTotalTickets(validateInput(totTicketMsg, 0, 100));
                config.setTicketReleaseRate(validateInput(relRateMsg, 0, 20));
                config.setCustomerRetrievalRate(validateInput(retRateMsg, 0, 20));
                config.setMaxTicketCapacity(validateInput(maxTicketMsg, 100, 1000));
                break;

            } else {
                System.out.println("Enter a valid input (yes/no)");
                user = input.nextLine();
            }
        }
        config.serializer();  // saving configuration object to a file
        saveToFile(config);   // saving the configuration details to a text file

        // Creating a ticket pool with an initial number of tickets
        TicketPool ticketPool = new TicketPool(config.getTotalTickets());
        ticketPool.setMaxCapacity(config.getMaxTicketCapacity());
        System.out.println("Max ticket capacity of the ticket pool : "+config.getMaxTicketCapacity());

        //prompting for the time the program should run
        long simulationTime = validateInput(simTimeMsg,10000,600000);

        //prompting for total vendors
        int vendorCount = validateInput(totVenMsg,0,10);
        if (vendorCount == 0){
            config.setTicketReleaseRate(0);
        }

        // prompting for total customers
        int customerCount = validateInput(totCusMsg,0,10);
        if (customerCount == 0) {
            config.setCustomerRetrievalRate(0);
        }

        // creating vendors
        for (int i = 0; i < vendorCount; i++) {
                Vendor vendor = new Vendor("Vendor-" + (i + 1), ticketPool, simulationTime);
           }

        // creating customers
        for (int i = 0; i < customerCount; i++) {
            Customer customer = new Customer("Customer-" + (i + 1), ticketPool, simulationTime);
        }

        // prompting for vendor cooling period
        if (vendorCount>0) {
            Vendor.setVendorCoolingTime(validateInput(venCoolTimeMsg, 500, 10000));
        }

        // prompting for customer cooling period
        if (customerCount>0) {
            Customer.setCustomerCoolingTime(validateInput(cusCoolTimeMsg, 500, 10000));
        }

        System.out.println("Customer Retrieval Rate : "+config.getCustomerRetrievalRate());
        System.out.println("Total Customers : "+Customer.getCustomerCount());

        // generating the number of tickets each customer purchases
        int[] customerTicketAlloc = generateRandom(config.getCustomerRetrievalRate(), Customer.getCustomerCount());
        for (int i = 0; i < Customer.getCustomerCount(); i++){
            System.out.println("Customer "+(i+1)+" : "+customerTicketAlloc[i]);
            Customer.getCustomers().get(i).setFixedPurchase(customerTicketAlloc[i]);
        }

        System.out.println("Ticket Release Rate : "+config.getTicketReleaseRate());
        System.out.println("Total Vendors : "+Vendor.getVendorCount());

        // generating the number tickets each vendor releases
        int[] vendorTicketAlloc = generateRandom(config.getTicketReleaseRate(), Vendor.getVendorCount());
        for (int i = 0; i < Vendor.getVendorCount(); i++){
            System.out.println("Vendor "+(i+1)+" : "+vendorTicketAlloc[i]);
            Vendor.getVendors().get(i).setFixedRelease(vendorTicketAlloc[i]);
        }



        // Start the threads for vendors and customers
        for (int i = 0; i < Vendor.getVendors().size(); i++){
            System.out.println("Starting vendors");
            Vendor.getVendors().get(i).start();
        }

        for (int i = 0; i < Customer.getCustomers().size(); i++){
            System.out.println("Starting customers");
            Customer.getCustomers().get(i).start();
        }

        // joining vendor and customer threads
        try {
            for (int i = 0; i < Vendor.getVendors().size(); i++){
                Vendor.getVendors().get(i).join();
            }

            for (int i = 0; i < Customer.getCustomers().size(); i++){
                Customer.getCustomers().get(i).join();
            }

        } catch (InterruptedException e) {
            System.out.println("Error");
            Thread.currentThread().interrupt();
        }


        logger.info("Simulation completed");
        System.out.println("\nSimulation Completed\nTotal available tickets : "+ticketPool.getAvailableTickets().size()+
                "\nTotal Sold Tickets : "+TicketPool.getSoldTickets());


        System.out.println("\nCustomer Details:");
        Customer.customerDetails();
        System.out.println("\nVendor Details:");
        Vendor.vendorDetails();

    }


    public static int validateInput(String message, int minValue, int maxvalue) {

        int value = -1;  // Initializing with an invalid value

        try {
            // Loop until a valid value is entered
            while (value < minValue || value > maxvalue) {
                System.out.println(message+" (between "+minValue+" and "+maxvalue+") : ");

                String userInput = input.nextLine().trim();  // Read input and remove leading/trailing spaces

                // Check if the input is empty or consists only of spaces
                if (userInput.isEmpty()) {
                    logger.warning("Empty input received");
                    System.out.println("Input cannot be empty or just spaces. Please try again.");
                    continue;
                }

                try {
                    // Attempt to parse the input as an integer
                    value = Integer.parseInt(userInput);

                    // Check if the entered number is within the valid range
                    if (value < minValue || value > maxvalue) {
                        logger.warning("Input out of range");
                        System.out.println("Number must be between" + minValue + " and " + maxvalue + ". Please try again.");

                    }
                } catch (NumberFormatException e) {
                    // If parsing fails, catch the exception and prompt for a valid number
                    logger.warning("Input is not a number");
                    System.out.println("Invalid input. Please enter a valid integer.");
                }
            }

            // Split the string and join the required parts
            String result = String.join(" ", Arrays.copyOfRange(message.split(" "), 2, message.split(" ").length));
            logger.info(result + " : " + value);

        } catch (Exception e) {
            System.out.println("An error occurred. Please try again.");
        }
        return value;
    }

    public static void saveToFile(Configuration config) {
        File file = new File("configDetails.txt");

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(
                    "Total tickets : " + config.getTotalTickets() +
                            "\nTicket release rate : " + config.getTicketReleaseRate() +
                            "\nCustomer Retrieval Rate : " + config.getCustomerRetrievalRate() +
                            "\nMax Ticket Capacity : " + config.getMaxTicketCapacity()
            );
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println("Error while saving to file");
            e.printStackTrace();
        }
    }

    public static int[] generateRandom(int rate, int count) {
        int[] array = new int[count];
        Random rand = new Random();

        if (rate >= count) {
            // If rate >= count, we need to assign values such that the sum equals rate
            // First, initialize all elements to 1 (this ensures the sum starts at 'count')
            for (int i = 0; i < count; i++) {
                array[i] = 1;
            }

            // Now, the sum is 'count', we need to add 'rate - count' more to the array
            int remaining = rate - count;

            // Randomly distribute the remaining sum
            while (remaining > 0) {
                int index = rand.nextInt(count);  // Random index
                array[index] += 1;  // Increment the value at that index
                remaining--;  // Decrease the remaining sum to distribute
            }
        } else {
            // If rate < count, we place exactly 'rate' number of 1's and the rest 0's
            for (int i = 0; i < count; i++) {
                array[i] = 0;  // Initialize all values to 0
            }

            // Place 'rate' number of 1's randomly
            for (int i = 0; i < rate; i++) {
                int index;
                do {
                    index = rand.nextInt(count);  // Random index to place a 1
                } while (array[index] == 1);  // Ensure no duplicate 1's are placed
                array[index] = 1;
            }
        }
        return array;
    }

}


