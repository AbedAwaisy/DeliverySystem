import java.util.List;
import java.util.Map;

class Scheduler extends Thread {
    private  UnboundedBuffer<ServiceCall> serviceCallsQueue;
    private  Map<String ,List<Vehicle>> Vehicles;
    private  InformationSystem infoSystem;
    private String area;
    private String id;
    private static boolean stopSignal ;
    private int salary;
    private final int salaryPerSec = 3;
    private Manager manager;


    public Scheduler(InformationSystem is, UnboundedBuffer<ServiceCall> serviceCalls, Map<String ,List<Vehicle>> vehicles, String ID, String area,Manager m) {
        this.serviceCallsQueue = serviceCalls;
        this.Vehicles = vehicles;
        this.infoSystem = is;
        this.area = area;
        this.id = ID;
        this.manager=m;
        //reset static fields
        stopSignal=false;

    }
    private void updateSalary(long sec) {
        int sal = (int)sec*salaryPerSec;
        manager.update_from_Scheduler(sal);
        this.salary += sal;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while (!stopSignal) {
            // Process objects from the queue
            try {

                ServiceCall serviceCall = serviceCallsQueue.dequeue();
                if (serviceCall != null){

                    // Check if the areas match and perform desired logic
                    if (area.equals(serviceCall.getServiceArea())) {
                        // Process the service call
                        Vehicle vehicle = Vehicles.get(serviceCall.getServiceType()).remove(0);
                        try {
                            Thread.sleep((int)(25*vehicle.calculateDrivingTime(serviceCall.getDistance())));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // insert to information system [vehicle, service call]
                        infoSystem.insert(serviceCall, vehicle);
                        System.out.println("New service call "+serviceCall.getTrackingNumber()+" arrived and data inserted to database");

                    } else {
                        // Areas don't match, return the service call to the queue
                        serviceCallsQueue.enqueue(serviceCall);
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        long seconds = duration / 1000;
        updateSalary(seconds);
    }

    public int getSalary() {
        return salary;
    }

    public static void setStopSignal() {
        stopSignal = true;
    }
}
