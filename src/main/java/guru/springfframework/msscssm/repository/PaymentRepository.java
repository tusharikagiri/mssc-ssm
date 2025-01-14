package guru.springfframework.msscssm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import guru.springfframework.msscssm.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
