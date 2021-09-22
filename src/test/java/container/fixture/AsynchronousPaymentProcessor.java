package container.fixture;

import container.MiniDi;

@Asynchronous
@MiniDi
public class AsynchronousPaymentProcessor implements PaymentProcessor{
    @Override
    public String pay() {
        return "AsynchronousPaymentProcessor";
    }
}
