import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Manager extends Thread {
    private UnboundedBuffer<Request> specialQueue;
    private InformationSystem infoSys;
    private ArrayList<Customer> customers;
    private Map<String ,List<Vehicle>> vehicles;

    public boolean isDone;
    private boolean driverUp;
    private int allRequests;
    private int doneRequests;
    private int totalFees;
    private UnboundedBuffer<ServiceCall>serviceCalls;
    private BoundedBuffer<ReadyRide> readyRides;


    private int Jerusalem;
    private int Tel_Aviv;
    private int SchedulerSalary;
    private int CarOfficerSalary;
    private int numberOfDrivers;
    private int DriversProfits;
    private int Delivery;
    private int clerksProfits;
    private int Taxi;
    private  UnboundedBuffer<List<Object>> deliveries;
    private  UnboundedBuffer<List<Object>> taxis;



    public Manager(UnboundedBuffer<List<Object>> Deliveries, UnboundedBuffer<List<Object>> Taxis,int NumberOfDrivers,UnboundedBuffer<Request> queue, UnboundedBuffer<ServiceCall>servicecalls, BoundedBuffer<ReadyRide> readyrides,  InformationSystem info,ArrayList<Customer> customers, Map<String ,List<Vehicle>> Vehicles, int allRequests) {
        this.specialQueue = queue;
        this.allRequests = allRequests;
        this.infoSys = info;
        this.customers = customers;
        this.vehicles = Vehicles;
        this.doneRequests = 0 ;
        this.isDone = false;
        this.driverUp=false;
        this.totalFees=0;
        this.serviceCalls=servicecalls;
        this.readyRides=readyrides;
        this.Jerusalem=0;
        this.Tel_Aviv=0;
        this.SchedulerSalary=0;
        this.CarOfficerSalary=0;
        this.DriversProfits=0;
        this.clerksProfits=0;
        this.Delivery=0;
        this.Taxi=0;
        this.numberOfDrivers=NumberOfDrivers;
        this.taxis = Taxis;
        this.deliveries = Deliveries;

    }
    public void update_from_Drivers(int profit){
        DriversProfits+=profit;
    }
    public void update_from_CarOfficer(int profit){
        CarOfficerSalary+=profit;
    }
    public void update_from_Scheduler(int profit){
        SchedulerSalary+=profit;
    }

    public void update_from_clerk(String area, String type){
        if(area.equals("Jerusalem")){Jerusalem++;}else Tel_Aviv++;
        if(type.equals("Taxi"))Taxi++;else Delivery++;
    }
    public void update_from_clerk(int salary) {
        clerksProfits+=salary;
    }


    public void addRequest() {
        doneRequests++;
    }

    @Override
    public void run() {
        while (!isDone) {
            try {
                Request request = specialQueue.dequeue();
                if (request != null) {
                    processRequest(request);
                }
                else {
                    if(driverUp){
                        driverUp=false;
                        specialQueue.driverUpdate(false);
                        if(doneRequests==allRequests){
                            endDay();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void processRequest(Request request) throws InterruptedException {
        // Simulating processing time of 3 seconds
        // Perform the processing logic here
        String customer_id = request.getCustomerID();
        double dist = request.getTravelDistance();
        String area = request.getServiceArea();
        String type = request.getServiceType();
        Customer customer = findCustomer(customer_id);
        ServiceCall serviceCall = new ServiceCall(customer, type, area, dist);
        Vehicle vehicle = vehicles.get(serviceCall.getServiceType()).remove(0);
        Thread.sleep(3000);
        infoSys.insert(serviceCall, vehicle);
        System.out.println("New Special Service Call "+ serviceCall.getTrackingNumber() +" Arrived !");
        // Update request status or perform any other required operations
//        doneRequests++;
//        isDone = doneRequests < allRequests;
    }
    private void endDay(){
        specialQueue.setTerminated();
        serviceCalls.setTerminated();
        Scheduler.setStopSignal();
        deliveries.setTerminated();
        taxis.setTerminated();
        CarOfficer.Done();
        readyRides.setTerminated();
        Driver.Done();
        Done();
        double numEm=(numberOfDrivers+8);
        String area;
        if(Jerusalem>Tel_Aviv)area="Jerusalem";
        else area="Tel Aviv";
        try {
            Thread.sleep( (3000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Main.green+"Salary Report:");
        System.out.println("Total Clerks Salary: $" + clerksProfits);
        System.out.println("Total Schedulers Salary: $" + SchedulerSalary);
        System.out.println("Total CarOfficers Salary: $" + CarOfficerSalary);
        System.out.println("Total Drivers Salary: $" + DriversProfits);
        double averageSalary1 =(SchedulerSalary+CarOfficerSalary+DriversProfits+clerksProfits)/numEm;
        System.out.println("Average Salary: $" + averageSalary1);
        System.out.println("Number of Delivery Trips: " + Delivery);
        System.out.println("Number of Taxi Trips: " + Taxi);
        System.out.println("Most Popular Service Area: " + area);
        System.out.println("done"+Main.reset);


    }

    private Customer findCustomer(String id){
        Customer foundCustomer = null;
        for (Customer customer : customers) {
            if (customer.getCustomer_id().equals(id)) {
                foundCustomer = customer;
                break;
            }
        }
        return foundCustomer;
    }

    public void Done(){
        isDone = true;
    }
    public synchronized void Update(){
        driverUp=true;
        addRequest();
        specialQueue.driverUpdate(true);
    }
}
