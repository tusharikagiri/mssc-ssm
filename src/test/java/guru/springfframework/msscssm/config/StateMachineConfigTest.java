package guru.springfframework.msscssm.config;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import guru.springfframework.msscssm.domain.PaymentEvent;
import guru.springfframework.msscssm.domain.PaymentState;

@SpringBootTest
class StateMachineConfigTest {

	@Autowired
	StateMachineFactory<PaymentState, PaymentEvent> factory;

	@Test
	void testNewStateMachine() {
		StateMachine<PaymentState, PaymentEvent> sm = factory.getStateMachine();
		sm.start();

		System.out.println(sm.getState().toString());

		sm.sendEvent(PaymentEvent.PRE_AUTHORIZE);

		System.out.println(sm.getState().toString());

		sm.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);

		System.out.println(sm.getState().toString());
	}

}
