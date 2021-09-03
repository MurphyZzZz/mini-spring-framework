package fixture;

@Synchronous
public class SynchronousPaymentProcessor implements PaymentProcessor{
    @Override
    public String pay() {
        return "SynchronousPaymentProcessor";
    }
}
