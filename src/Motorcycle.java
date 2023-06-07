public class Motorcycle extends Vehicle {
    public Motorcycle() {
        super(); // Call the base class constructor (Vehicle)
        LicenseType = "A";
    }
    public String getLicenseType(){return LicenseType;}
    @Override
    public double calculateDrivingTime(double distance){
        double p = Math.random() * (0.8 - 0.6) + 0.6;
        return distance / (speed * p);
    }
}
