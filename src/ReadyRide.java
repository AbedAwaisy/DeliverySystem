
public class ReadyRide {
    private ServiceCall serviceCall;
    private Vehicle vehicle;

    public ReadyRide(ServiceCall serviceCall, Vehicle vehicle) {
        this.serviceCall = serviceCall;
        this.vehicle = vehicle;
    }

    public ServiceCall getServiceCall() {
        return serviceCall;
    }

    public void setServiceCall(ServiceCall serviceCall) {
        this.serviceCall = serviceCall;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
