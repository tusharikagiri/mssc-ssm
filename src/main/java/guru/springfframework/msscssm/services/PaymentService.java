package guru.springfframework.msscssm.services;

import org.springframework.statemachine.StateMachine;

import guru.springfframework.msscssm.domain.Payment;
import guru.springfframework.msscssm.domain.PaymentEvent;
import guru.springfframework.msscssm.domain.PaymentState;

public interface PaymentService {
	
	public Payment newPayment(Payment payment);
	
	public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);
	
	public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);
	
	public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);

}
