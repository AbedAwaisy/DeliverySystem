import java.util.ArrayList;
import java.util.List;

public class Clerk extends Thread {
    private  UnboundedBuffer<List<Object>> requests;
    private  UnboundedBuffer<ServiceCall> serviceCalls;
    private  UnboundedBuffer<Request> specialRequests;
    private String id;
    private static boolean status;


    private  ArrayList<Customer> Allcustomers;
    private final int salaryPerReq = 4;
    private int  allRequests;
    private static int  DoneRequests=0;
    private int salary;
    private Manager manager;

   /* public int getSalary() {
        return salary;
    }*/

    public Clerk(UnboundedBuffer<List<Object>> reqs, UnboundedBuffer<ServiceCall> serviceCalls1, UnboundedBuffer<Request> specialReqs, ArrayList<Customer> customers, String ID, int allRequest,Manager m) {
        this.requests = reqs;
        this.serviceCalls = serviceCalls1;
        this.specialRequests = specialReqs;
        this.Allcustomers = customers;
        this.id = ID;
        this.allRequests=allRequest;
        this.salary = 0;
        this.manager=m;
        //reset static fields
        DoneRequests=0;
        status=true;


    }

    public void run() {
        while (isStatus()) {
            try {
                List<Object> requestInfo = requests.dequeue();
                if (requestInfo != null) {
                    Request request = (Request) requestInfo.get(0);
                    double time = (double) requestInfo.get(1);
                    handleRequest(request, time);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        manager.update_from_clerk(salary);
        requests.setTerminated();
    }

    private void handleRequest(Request request, double time) throws InterruptedException {
        // Process the request
        String customer_id = request.getCustomerID();
        double dist = request.getTravelDistance();
        String area = request.getServiceArea();
        String type = request.getServiceType();
        // Update request status or perform any other required operations
        Customer customer = findCustomer(customer_id);
        if (customer == null){
            // create
            customer = new Customer(customer_id);
            Allcustomers.add(customer);
        }

        if (dist < 100){
            // create service call object and insert into services queue
            ServiceCall serviceCall = new ServiceCall(customer, type, area, dist);
            Thread.sleep((long) (time * 1000)); // Sleep for the specified time
            serviceCalls.enqueue(serviceCall);

        }else{
            // insert into manager's queue
            Thread.sleep(500); // Sleep for the specified time
            specialRequests.enqueue(request);
        }
        DoneRequests++;
        status = DoneRequests<allRequests;
        updateSalary();
        manager.update_from_clerk(area,type);
    }
    public static boolean isStatus() {
        return status;
    }

    private void updateSalary() {
        this.salary += salaryPerReq;
    }

    private Customer findCustomer(String id){
        Customer foundCustomer = null;
        for (Customer customer : Allcustomers) {
            if (customer.getCustomer_id().equals(id)) {
                foundCustomer = customer;
                break;
            }
        }
        return foundCustomer;
    }


//    public void setId(String id) {
//        this.id = id;
//    }

}
