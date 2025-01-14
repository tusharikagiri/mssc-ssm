package guru.springfframework.msscssm.services;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import guru.springfframework.msscssm.domain.Payment;
import guru.springfframework.msscssm.domain.PaymentEvent;
import guru.springfframework.msscssm.domain.PaymentState;
import guru.springfframework.msscssm.repository.PaymentRepository;

@SpringBootTest
class PaymentServiceImplTest {

	@Autowired
	PaymentService paymentService;
	
	@Autowired
	PaymentRepository paymentRepository; 

	private Payment payment;

	@BeforeEach
	void setUp() {
		payment = Payment.builder().amount(new BigDecimal("12.99")).build();
	}

	@Test
	@Transactional
	void preAuth() {
		Payment savedPayment = paymentService.newPayment(payment);
		
		System.out.println("Should be NEW");
		System.out.println(savedPayment.getState());

		StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
		
		Optional<Payment> preAuthedPayment = paymentRepository.findById(savedPayment.getId());
		
		System.out.println("Should be PRE_AUTH");
		System.out.println(sm.getState().getId());
		System.out.println(preAuthedPayment);
		
		assertNotNull(preAuthedPayment.get());
	}
	
	@RepeatedTest(10)
	@Transactional
	void auth() {
		Payment savedPayment = paymentService.newPayment(payment);
		
		System.out.println("Should be NEW");
		System.out.println(savedPayment.getState());

		StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
		
		Optional<Payment> preAuthedPayment = paymentRepository.findById(savedPayment.getId());
		
		if(savedPayment.getState() == PaymentState.PRE_AUTH) {
			sm = paymentService.authorizePayment(savedPayment.getId());
		}
		System.out.println("Should be AUTH");
		System.out.println(sm.getState().getId());
		System.out.println(preAuthedPayment);
		
		assertNotNull(preAuthedPayment.get());
	}

}
