package minidi.container.fixture;

import minidi.container.MiniDi;

@Asynchronous
@MiniDi
public class AsynchronousPaymentProcessor implements PaymentProcessor{
    @Override
    public String pay() {
        return "AsynchronousPaymentProcessor";
    }
}
