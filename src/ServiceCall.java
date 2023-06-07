
public class ServiceCall implements Comparable<ServiceCall>{

    private Customer customer;
    private String serviceArea;
    private double distance;
    private String serviceType;
    private static int tracking=1;
    private int trackingNumber;

    // constructor for all service calls.
    public ServiceCall(Customer customer,String type, String serviceArea, double distance) {
        this.trackingNumber=tracking++;
        this.customer = customer;
        this.serviceType=type;
        this.serviceArea = serviceArea;
        this.distance = distance;
    }

    public int getTrackingNumber() {
        return trackingNumber;
    }

//    public void setTrackingNumber(int trackingNumber) {
//        this.trackingNumber = trackingNumber;
//    }
    // getters
    public Customer getCustomer() {
        return customer;
    }

//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }

//    public void setServiceArea(String serviceArea) {
//        this.serviceArea = serviceArea;
//    }

    public double getDistance() {
        return distance;
    }

//    public void setDistance(double distance) {
//        this.distance = distance;
//    }

    @Override
    public String toString() {
        return "ServiceCall{" +
                "trackingNumber=" + trackingNumber +
                ", serviceType='" + serviceType + '\'' +
                ", serviceArea='" + serviceArea + '\'' +
                ", distance=" + distance +
                '}';
    }

    public String getServiceType() {
        return serviceType;
    }

//    public void setServiceType(String serviceType) {
//        this.serviceType = serviceType;
//    }


    public String getServiceArea() {
        return serviceArea;
    }
    @Override
    public int compareTo(ServiceCall other) {
        if (this.distance < other.distance) {
            return -1;
        } else if (this.distance > other.distance) {
            return 1;
        }
        return 0;
    }

}

