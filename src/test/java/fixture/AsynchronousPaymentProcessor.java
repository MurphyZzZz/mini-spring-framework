package fixture;

@Asynchronous
public class AsynchronousPaymentProcessor implements PaymentProcessor{
    @Override
    public String pay() {
        return "AsynchronousPaymentProcessor";
    }
}
