package backend.finance.adapters.gateways;

import backend.finance.adapters.presenters.CustomerPresenter;
import backend.finance.adapters.repositories.CustomerRepository;
import backend.finance.application.dtos.CustomerDto;
import backend.finance.application.ports.output.CustomerSaveOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CustomerSaveGateway implements CustomerSaveOutputPort {

    private final CustomerRepository customerRepository;

    private final CustomerPresenter customerPresenter;

    @Transactional
    @Override
    public CustomerDto save(CustomerDto dto) {
        var customerJpa = customerPresenter.toJpa(dto);
        var customerSave = customerRepository.save(customerJpa);
        return customerPresenter.toDto(customerSave);
    }
}
