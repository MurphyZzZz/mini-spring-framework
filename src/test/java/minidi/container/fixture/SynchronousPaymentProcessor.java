package minidi.container.fixture;

import minidi.container.MiniDi;

@Synchronous
@MiniDi
public class SynchronousPaymentProcessor implements PaymentProcessor{
    @Override
    public String pay() {
        return "SynchronousPaymentProcessor";
    }
}
