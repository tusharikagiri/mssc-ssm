package guru.springfframework.msscssm.config;

import java.util.EnumSet;
import java.util.Random;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import guru.springfframework.msscssm.config.actions.AuthAction;
import guru.springfframework.msscssm.config.actions.AuthApprovedAction;
import guru.springfframework.msscssm.config.actions.AuthDeclinedAction;
import guru.springfframework.msscssm.config.actions.PreAuthAction;
import guru.springfframework.msscssm.config.actions.PreAuthApprovedAction;
import guru.springfframework.msscssm.config.actions.PreAuthDeclinedAction;
import guru.springfframework.msscssm.config.guards.PaymentIdGuard;
import guru.springfframework.msscssm.domain.PaymentEvent;
import guru.springfframework.msscssm.domain.PaymentState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {
	
	private final PaymentIdGuard paymentIdGuard;
	private final AuthAction authAction;
	private final AuthApprovedAction authApprovedAction;
	private final AuthDeclinedAction authDeclinedAction;
	private final PreAuthAction preAuthAction;
	private final PreAuthApprovedAction preAuthApprovedAction;
	private final PreAuthDeclinedAction preAuthDeclinedAction;

	@Override
	public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
		states.withStates().initial(PaymentState.NEW).states(EnumSet.allOf(PaymentState.class)).end(
				PaymentState.AUTH).end(PaymentState.PRE_AUTH_ERROR).end(PaymentState.AUTH_ERROR);
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
		transitions
		.withExternal()
		.source(PaymentState.NEW)
		.target(PaymentState.NEW)
		.event(PaymentEvent.PRE_AUTHORIZE)
		.action(preAuthAction)
		.guard(paymentIdGuard)
		.and()
		.withExternal()
		.source(PaymentState.NEW)
		.target(PaymentState.PRE_AUTH)
		.event(PaymentEvent.PRE_AUTH_APPROVED)
		.action(preAuthApprovedAction)
		.and()
		.withExternal()
		.source(PaymentState.NEW)
		.target(PaymentState.PRE_AUTH_ERROR)
		.event(PaymentEvent.PRE_AUTH_DECLINED)
		.action(preAuthDeclinedAction)
		.and()
		.withExternal()
		.source(PaymentState.PRE_AUTH)
		.target(PaymentState.PRE_AUTH)
		.event(PaymentEvent.AUTHORIZE)
		.action(authAction)
		.and()
		.withExternal()
		.source(PaymentState.PRE_AUTH)
		.target(PaymentState.AUTH).event(PaymentEvent.AUTH_APPROVED)
		.action(authApprovedAction)
		.and()
		.withExternal()
		.source(PaymentState.PRE_AUTH)
		.target(PaymentState.AUTH_ERROR)
		.event(PaymentEvent.AUTH_DECLINED)
		.action(authDeclinedAction);
	}

	@Override
	public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {
		StateMachineListenerAdapter<PaymentState, PaymentEvent> adapter = new StateMachineListenerAdapter<PaymentState, PaymentEvent>() {
			@Override
			public void stateChanged(State<PaymentState, PaymentEvent> from, State<PaymentState, PaymentEvent> to) {
				log.info(String.format("stateChanged(from: %s, to: %s", from, to));
			}
		};

		config.withConfiguration().listener(adapter);
	}
}
