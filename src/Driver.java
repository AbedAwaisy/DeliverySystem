import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;


public class Driver extends Thread {
    private String licenseType;
    private  BoundedBuffer<ReadyRide> readyRides;
    private static boolean isDone;
    private  Map<String , List<Vehicle>> Vehicles;
    private Manager manager;
    private int DriverRate;
    private int totalRates;
    private int driverProfit;
    private int TotalDistanceDrove;
    private String id;




    public Driver(String licenseType, BoundedBuffer<ReadyRide> readyrides, Map<String , List<Vehicle>> vehicles,Manager manager1,int i) {
        this.licenseType = licenseType;
        this.readyRides = readyrides;
        this.Vehicles = vehicles;
        id="Driver "+i;
        manager=manager1;
        totalRates=0;
        DriverRate=0;
        driverProfit=0;
        TotalDistanceDrove=0;
        //reset static fields
        isDone=false;

    }

    public int getPayment(){
        return driverProfit+2*TotalDistanceDrove;
    }
    public void gotFee(){
        driverProfit+=10;
    }

    public String getLicenseType() {
        return licenseType;
    }
    public void updateProfit(int rate, double time) {
        double p;
        Random random = new Random();
        p= random.nextInt(2);
        if(p==0)p=0.5;
        driverProfit+=(int)(2*time+rate-(time*p));

    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    @Override
    public void run() {
        while (!isDone) {
            try {
                ReadyRide readyRide = readyRides.dequeue(); // Wait for an object to be inserted into the queue
                if (readyRide != null){
                    if (!readyRide.getVehicle().LicenseType.equals(licenseType)){
                        readyRides.enqueue(readyRide);
                    }else{
                        processObject(readyRide);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        manager.update_from_Drivers(getPayment());
    }
    private void processObject(ReadyRide readyRide) {
        // Perform the processing logic here
        double driveTime = readyRide.getVehicle().calculateDrivingTime(readyRide.getServiceCall().getDistance());
        if (readyRide.getVehicle().LicenseType.equals("A")){
            Vehicles.get("Delivery").add(readyRide.getVehicle());
        }else{
            Vehicles.get("Taxi").add(readyRide.getVehicle());
        }
        Random random = new Random();
        int rate = random.nextInt(11);
        updateRate(rate);
        updateProfit(rate,driveTime);
        TotalDistanceDrove+=readyRide.getServiceCall().getDistance();
        if(readyRide.getServiceCall().getDistance()>=100){
            gotFee();
        }
        notifyManger();
        try {
            Thread.sleep((int) (driveTime*100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void updateRate(int rate){
        totalRates++;
        DriverRate+=rate;
    }

    public double getDriverRate() {
        return (double) DriverRate/totalRates;
    }

    public static void Done(){
        isDone = true;
    }
    public void notifyManger(){
        manager.Update();
    }
}
