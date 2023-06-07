import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    static String reset = "\u001B[0m";
    static String red = "\u001B[31m";
    static String green = "\u001B[32m";
    public static void main(String[] args) {
        MyGUI gui = new MyGUI();
        // Display the GUI
        gui.setVisible(true);
    }
    public static void start(int Drivers, double COfficerTime){
        // Create an instance of the GUI class
//        cleanupThreads();
        List<List<Object>> requestList = ReadFileToArray.readRequestsFromFile("src/Requests.txt");
        UnboundedBuffer<List<Object>> queue = new UnboundedBuffer<>();
        ArrayList<Thread> threads=new ArrayList<>();
        UnboundedBuffer<List<Object>> deliveries = new UnboundedBuffer<>();
        UnboundedBuffer<List<Object>> taxis = new UnboundedBuffer<>();

        UnboundedBuffer<ServiceCall> serviceCalls = new UnboundedBuffer<>();
        UnboundedBuffer<Request> specialRequests = new UnboundedBuffer<>();
        BoundedBuffer<ReadyRide> readyRides = new BoundedBuffer<>();

        ArrayList<Customer> customers = createCustomers(requestList);
        ArrayList<Driver> drivers = new ArrayList<>();
        InformationSystem infoSys = new InformationSystem(deliveries, taxis);

        //Vehicles
        Map<String ,List<Vehicle>> Vehicles = new HashMap<>();
        List<Vehicle> taxies = new ArrayList<>();
        List<Vehicle> Motorcycles = new ArrayList<>();
        for(int i=0;i<Drivers;i++){
            taxies.add(new Taxi());
            Motorcycles.add(new Motorcycle());
        }
        Vehicles.put("Taxi",taxies);
        Vehicles.put("Delivery",Motorcycles);

        // Create Clerk instances
        Manager manager = new Manager(deliveries,taxis,Drivers,specialRequests,serviceCalls,readyRides, infoSys, customers, Vehicles, requestList.size());

        Clerk clerk1 = new Clerk(queue,serviceCalls, specialRequests, customers,"Clerk 1", requestList.size(),manager);
        Clerk clerk2 = new Clerk(queue,serviceCalls, specialRequests, customers, "Clerk 2", requestList.size(),manager);
        Clerk clerk3 = new Clerk(queue,serviceCalls, specialRequests, customers, "Clerk 3", requestList.size(),manager);

        Scheduler scheduler1 = new Scheduler(infoSys, serviceCalls, Vehicles, "Scheduler 1", "Jerusalem",manager);
        Scheduler scheduler2 = new Scheduler(infoSys, serviceCalls, Vehicles, "Scheduler 2", "Tel Aviv",manager);
        CarOfficer carOfficer1 = new CarOfficer("Car Officer 1", deliveries, taxis, readyRides,manager, COfficerTime);
        CarOfficer carOfficer2 = new CarOfficer("Car Officer 2", deliveries, taxis, readyRides,manager, COfficerTime);
        CarOfficer carOfficer3 = new CarOfficer("Car Officer 3", deliveries, taxis, readyRides,manager, COfficerTime);
        Driver d;
        threads.add(carOfficer1);
        threads.add(carOfficer2);
        threads.add(carOfficer3);
        threads.add(scheduler2);
        threads.add(scheduler1);
        threads.add(clerk3);
        threads.add(clerk2);
        threads.add(clerk1);

        for(int i = 0 ;i < Drivers;i++){
            if(i%2==0){
                d= new Driver("A", readyRides, Vehicles,manager,i);
                drivers.add(d);
                threads.add(d);
            }else {
                d=new Driver("B", readyRides, Vehicles,manager,i);
                drivers.add(d);
                threads.add(d);
            }
        }
        threads.add(manager);


        // Start all
        for (Thread thread:threads){
            thread.start();
        }

        long startTime = System.currentTimeMillis();

        for (List<Object> requestData : requestList) {
            Request request = (Request) requestData.get(0);
            int arrival = (int) requestData.get(1);
            double time = (double) requestData.get(2);

            long delay = calculateDelay(startTime, arrival);

            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            List<Object> requestWithTime = new ArrayList<>();
            requestWithTime.add(request);
            requestWithTime.add(time);
            queue.enqueue(requestWithTime);
        }
        cleanupThreads(threads);


    }

    private static long calculateDelay(long startTime, int arrival) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        long delay = arrival * 1000 - elapsedTime;

        return Math.max(delay, 0);
    }
    private static void cleanupThreads(List<Thread>threads ) {
        if(threads.isEmpty()){return;}
        for (Thread thread : threads) {
            if (thread.isAlive()) {
                try {
                    thread.join(); // Wait for the thread to terminate
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static ArrayList<Customer> createCustomers(List<List<Object>> requestList){
        ArrayList<Customer> customers = new ArrayList<>();

        for (List<Object> request : requestList) {
            Object requestData = request.get(0); // Assuming the request object is at index 0
            if (requestData instanceof Request) {
                Request requestObj = (Request) requestData;
                String customerID = requestObj.getCustomerID();

                // Check if customer already exists in the customers list
                boolean customerExists = false;
                for (Customer customer : customers) {
                    if (customer.getCustomer_id().equals(customerID)) {
                        customerExists = true;
                        break;
                    }
                }

                // If customer doesn't exist, create and add it to the customers list
                if (!customerExists) {
                    Customer newCustomer = new Customer(customerID);
                    customers.add(newCustomer);
                }
            }
        }
        return customers;
    }
}
