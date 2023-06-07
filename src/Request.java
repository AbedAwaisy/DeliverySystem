public class Request {
    private String customerID;
    private String serviceType;
    private String serviceArea;
    private double travelDistance;

    // Constructor
    public Request(String customerID, String serviceType, String serviceArea, double travelDistance) {
        this.customerID = customerID;
        this.serviceType = serviceType;
        this.serviceArea = serviceArea;
        this.travelDistance = travelDistance;
    }

    @Override
    public String toString() {
        return "Request{" +
                "customerID='" + customerID + '\'';
    }

    // Getters and setters
    public String getCustomerID() {
        return customerID;
    }

//    public void setCustomerID(String customerID) {
//        this.customerID = customerID;
//    }

    public String getServiceType() {
        return serviceType;
    }

//    public void setServiceType(String serviceType) {
//        this.serviceType = serviceType;
//    }

    public String getServiceArea() {
        return serviceArea;
    }

//    public void setServiceArea(String serviceArea) {
//        this.serviceArea = serviceArea;
//    }

    public double getTravelDistance() {
        return travelDistance;
    }

//    public void setTravelDistance(double travelDistance) {
//        this.travelDistance = travelDistance;
//    }
}
