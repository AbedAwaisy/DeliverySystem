public class Taxi extends Vehicle {
    public Taxi() {
        super(); // Call the base class constructor (Vehicle)
        LicenseType = "B";
    }
    public String getLicenseType(){return LicenseType;}
    @Override
    public double calculateDrivingTime(double distance){
        double p = Math.random() * (0.7 - 0.5) + 0.5;
        return distance / (100 * p);
    }
}
