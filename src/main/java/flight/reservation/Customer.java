package flight.reservation;

import flight.reservation.flight.ScheduledFlight;
import flight.reservation.order.FlightOrder;
import flight.reservation.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO: introduce passenger for name? or Person as parent class
public class Customer extends Person {

    private String email;
    private List<Order> orders;

    public Customer(String name, String email) {
        super(name);
        this.email = email;
        this.orders = new ArrayList<>();
    }

    public FlightOrder createOrder(List<String> passengerNames, List<ScheduledFlight> flights, double price) {
        if (!isOrderValid(passengerNames, flights)) {
            throw new IllegalStateException("Order is not valid");
        }
        FlightOrder order = new FlightOrder(flights);
        order.setCustomer(this);
        order.setPrice(price);
        List<Passenger> passengers = passengerNames
                .stream()
                .map(Passenger::new)
                .collect(Collectors.toList());
        order.setPassengers(passengers);
        order.getScheduledFlights().forEach(scheduledFlight -> scheduledFlight.addPassengers(passengers));
        orders.add(order);
        return order;
    }

    private boolean isOrderValid(List<String> passengerNames, List<ScheduledFlight> flights) {
        boolean valid = true;
        valid = valid && !FlightOrder.getNoFlyList().contains(this.getName());
        valid = valid && passengerNames.stream().noneMatch(passenger -> FlightOrder.getNoFlyList().contains(passenger));
        valid = valid && flights.stream().allMatch(scheduledFlight -> {
            return scheduledFlight.getAvailableCapacity() >= passengerNames.size();
        });
        return valid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}

