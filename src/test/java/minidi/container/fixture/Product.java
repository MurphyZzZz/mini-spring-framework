package minidi.container.fixture;

import minidi.container.MiniDi;

import javax.inject.Inject;

@MiniDi
public class Product {
    public PaymentProcessor paymentProcessor;

    @Inject
    public Product(@Synchronous PaymentProcessor paymentProcessor){
        this.paymentProcessor = paymentProcessor;
    }
}
