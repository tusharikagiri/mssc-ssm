package guru.springfframework.msscssm.services;

import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import guru.springfframework.msscssm.domain.Payment;
import guru.springfframework.msscssm.domain.PaymentEvent;
import guru.springfframework.msscssm.domain.PaymentState;
import guru.springfframework.msscssm.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

	private final PaymentRepository paymentRepository;

	@Override
	public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message,
			Transition<PaymentState, PaymentEvent> transition, StateMachine<PaymentState, PaymentEvent> stateMachine) {
		Optional.ofNullable(message).ifPresent(msg -> {
			Optional.ofNullable(
					Long.class.cast(msg.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID_HEADER, -1L)))
					.ifPresent(paymentId -> {
						Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new EntityNotFoundException());
						payment.setState(state.getId());
						paymentRepository.save(payment);
						System.out
								.println("Saved payment :" + payment);
					});
		});
	}
}
