package guru.springfframework.msscssm.config.actions;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import guru.springfframework.msscssm.domain.PaymentEvent;
import guru.springfframework.msscssm.domain.PaymentState;

@Component
public class AuthApprovedAction implements Action<PaymentState, PaymentEvent> {

	@Override
	public void execute(StateContext<PaymentState, PaymentEvent> context) {
		System.out.println("AuthApprovedAction action is called!!!");		
	}

}
