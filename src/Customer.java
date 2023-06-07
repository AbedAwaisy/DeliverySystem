public class Customer {
    private String customer_id;

    public Customer(String id) {
        this.customer_id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customer_id=" + customer_id +
                '}';
    }
}
