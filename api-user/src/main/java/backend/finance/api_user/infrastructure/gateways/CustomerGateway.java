package backend.finance.api_user.infrastructure.gateways;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.jpas.RoleJpa;
import backend.finance.api_user.infrastructure.ports.output.CustomerOutputPort;
import backend.finance.api_user.infrastructure.presenters.CustomerPresenter;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import backend.finance.api_user.infrastructure.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerGateway implements CustomerOutputPort {

    private final CustomerRepository customerRepository;

    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public CustomerDto save(CustomerRequest customerRequest) {
        var roleJpa = getOrCreateRole(customerRequest.user().role());
        var customerJpa = CustomerPresenter.toCustomerJpa(customerRequest, roleJpa);
        var customerSalvo = customerRepository.save(customerJpa);
        return CustomerPresenter.toCustomerDto(customerSalvo);
    }

    private RoleJpa getOrCreateRole(String name) {
        try {
            var roleEnum = RoleEnum.valueOf(name);
            var roleJpa = roleRepository.findByName(roleEnum);
            return roleJpa.orElseGet(() -> roleRepository.save(new RoleJpa(null, roleEnum)));
        } catch (IllegalArgumentException e) {
            // TODO - Add Custom Exception
            throw new RuntimeException("Role '" + name + "' does not exist", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CustomerDto> findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(CustomerPresenter::toCustomerDto);
    }
}
