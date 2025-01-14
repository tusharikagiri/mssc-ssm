package guru.springfframework.msscssm.services;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guru.springfframework.msscssm.domain.Payment;
import guru.springfframework.msscssm.domain.PaymentEvent;
import guru.springfframework.msscssm.domain.PaymentState;
import guru.springfframework.msscssm.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
	public static final String PAYMENT_ID_HEADER = "payment_id";

	private final PaymentRepository paymentRepository;
	private final StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;

	@Override
	public Payment newPayment(Payment payment) {
		payment
				.setState(PaymentState.NEW);
		return paymentRepository
				.save(payment);
	}

	@Override
	@Transactional
	public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) {
		StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);

		sendEvent(paymentId, sm, PaymentEvent.PRE_AUTHORIZE);

		return sm;
	}

	@Override
	@Transactional
	public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) {
		StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);

		sendEvent(paymentId, sm, PaymentEvent.AUTHORIZE);

		return sm;
	}

	@Override
	@Transactional
	public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) {
		StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);

		sendEvent(paymentId, sm, PaymentEvent.AUTH_DECLINED);

		return sm;
	}

	private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> sm, PaymentEvent event) {
		Message<PaymentEvent> msg = MessageBuilder
				.withPayload(event)
				.setHeader(PAYMENT_ID_HEADER, paymentId)
				.build();

		sm
				.sendEvent(msg);
	}

	private StateMachine<PaymentState, PaymentEvent> build(Long paymentId) {
		Payment payment = paymentRepository
				.findById(paymentId)
				.orElseThrow(() -> new EntityNotFoundException("Payment data not found."));

		StateMachine<PaymentState, PaymentEvent> sm = stateMachineFactory
				.getStateMachine(Long
						.toString(payment
								.getId()));

		sm
				.stop();

		sm
				.getStateMachineAccessor()
				.doWithAllRegions(sma -> {
					sma
							.resetStateMachine(new DefaultStateMachineContext<PaymentState, PaymentEvent>(payment
									.getState(), null, null, null));
				});

		sm
				.start();

		return sm;
	}

}
