package guru.springfframework.msscssm.config.actions;

import java.util.Random;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import guru.springfframework.msscssm.domain.PaymentEvent;
import guru.springfframework.msscssm.domain.PaymentState;
import guru.springfframework.msscssm.services.PaymentServiceImpl;

@Component
public class AuthAction implements Action<PaymentState, PaymentEvent> {

	@Override
	public void execute(StateContext<PaymentState, PaymentEvent> context) {
		System.out.println("Auth action is called!!!");

		PaymentEvent payloadEvent = PaymentEvent.AUTH_DECLINED;

		if (new Random().nextInt(10) < 8) {
			System.out.println("Approved");
			payloadEvent = PaymentEvent.AUTH_APPROVED;

		} else {
			System.out.println("Declined! No Credit!!!!!!");

		}
		context.getStateMachine().sendEvent(
				MessageBuilder.withPayload(payloadEvent).setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER,
						context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER)).build());		
	}

}
