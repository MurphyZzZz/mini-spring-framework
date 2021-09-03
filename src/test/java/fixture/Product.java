package fixture;

import javax.inject.Inject;

public class Product {
    public PaymentProcessor paymentProcessor;

    @Inject
    public Product(@Synchronous PaymentProcessor paymentProcessor){
        this.paymentProcessor = paymentProcessor;
    }
}
