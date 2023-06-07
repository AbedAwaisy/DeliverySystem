import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InformationSystem {
    private List<List<Object>> taxis;
    private List<List<Object>> motorcycles;
    private  UnboundedBuffer<List<Object>> deliveriesQueue;
    private  UnboundedBuffer<List<Object>> taxisQueue;
    private Random random;


    public InformationSystem(UnboundedBuffer<List<Object>> Deliveries, UnboundedBuffer<List<Object>> Taxis) {
        taxis = new ArrayList<>();
        motorcycles = new ArrayList<>();
        random = new Random();
        deliveriesQueue = Deliveries;
        taxisQueue = Taxis;
    }

    public synchronized void insert(ServiceCall sCall, Vehicle v) {
        List<Object> pair = new ArrayList<>();
        pair.add(sCall);
        pair.add(v);
        if (v.getLicenseType().equals("B")){
            taxis.add(pair);
            //databse

            taxisQueue.enqueue(pair);
        }else{
            motorcycles.add(pair);
            deliveriesQueue.enqueue(pair);
        }

    }

    @Override
    public String toString() {
        return "InformationSystem{" +
                "taxis=" + taxis.size() +
                ", motorcycles=" + motorcycles.size() +
                '}';
    }
    public boolean isEmpty(){
        return taxis.isEmpty() && motorcycles.isEmpty();
    }
    public void getone(){
        if (!taxis.isEmpty()){
            System.out.println("taxi: " + ((ServiceCall)taxis.get(0).get(0)).getCustomer().getCustomer_id());
        }
        if (!motorcycles.isEmpty()){
            System.out.println("motor: " + ((ServiceCall)motorcycles.get(0).get(0)).getCustomer().getCustomer_id());
        }
    }
}
