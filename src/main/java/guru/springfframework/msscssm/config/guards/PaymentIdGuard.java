package guru.springfframework.msscssm.config.guards;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

import guru.springfframework.msscssm.domain.PaymentEvent;
import guru.springfframework.msscssm.domain.PaymentState;
import guru.springfframework.msscssm.services.PaymentServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentIdGuard implements Guard<PaymentState, PaymentEvent> {

	@Override
	public boolean evaluate(StateContext<PaymentState, PaymentEvent> context) {
		log.info("Checking payment id guard for Id : " + PaymentServiceImpl.PAYMENT_ID_HEADER);
		return context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER) != null;
	}
}
