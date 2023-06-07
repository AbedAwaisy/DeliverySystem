import java.util.Random;

public class Vehicle {
    protected int speed;
    protected String LicenseType;
    public Vehicle() {
        // Generate a random speed between 70 and 130
        Random random = new Random();
        this.speed = random.nextInt(61) + 70;
    }

    public int getSpeed() {
        return speed;
    }
    public String getLicenseType(){return LicenseType;}
    public double calculateDrivingTime(double distance) {return 0.0;}

    }
