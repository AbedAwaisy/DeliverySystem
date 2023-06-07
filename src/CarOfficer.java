import java.util.List;
import java.util.Random;

public class CarOfficer extends Thread {
    private String  id;
    private UnboundedBuffer<List<Object>> deliveries;
    private UnboundedBuffer<List<Object>> taxis;
    private BoundedBuffer<ReadyRide> readyRides;
    private static boolean isDone;
    private int salary;
    private Manager manager;
    private double proccessTime;
    Random random ;

    public CarOfficer(String id, UnboundedBuffer<List<Object>> Deliveries, UnboundedBuffer<List<Object>> Taxis, BoundedBuffer<ReadyRide> ReadyRides,Manager m, double ProcTime) {
        this.id = id;
        this.taxis = Taxis;
        this.deliveries = Deliveries;
        this.readyRides = ReadyRides;
        this.manager=m;
        this.proccessTime = ProcTime;
        random= new Random();
        //reset static fields
        isDone=false;
    }
    private void updateSalary() {
        this.salary += 5;
    }

    public int getSalary() {
        return salary;
    }

    public String getid() {
        return id;
    }
    public void gotFee(){
        salary+=10;
        manager.update_from_CarOfficer(10);
    }
    @Override
    public void run() {
        while (!isDone) {
            List<Object> item = null;
            double probability = random.nextDouble();
            if (probability < 0.5 && !taxis.isEmpty()) {
                try {
                    item = taxis.dequeue();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (probability >= 0.5 && !deliveries.isEmpty()) {
                try {
                    item = deliveries.dequeue();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (item != null){
                updateSalary();
                processValue(item);
            }
        }
        manager.update_from_CarOfficer(salary);
    }

    private void processValue(List<Object> value) {
        // Perform the processing logic here
        ReadyRide readRide = new ReadyRide((ServiceCall) value.get(0), (Vehicle) value.get(1));
        System.out.println(id + " Salary: "+salary +" "+ value.get(0));
        try {
            Thread.sleep((long) (proccessTime*1000));
            if(readRide.getServiceCall().getDistance()>=100){
                gotFee();
            }
            readyRides.enqueue(readRide);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void Done(){
        isDone = true;
    }

}
