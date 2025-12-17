package backend.finance.adapters.gateways;

import backend.finance.adapters.presenters.CustomerPresenter;
import backend.finance.adapters.repositories.CustomerRepository;
import backend.finance.application.dtos.CustomerDto;
import backend.finance.application.dtos.response.CustomerAllResponse;
import backend.finance.application.ports.output.CustomerQueryOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomerQueryGateway implements CustomerQueryOutputPort, CustomerPagePort {

    private final CustomerRepository customerRepository;

    private final CustomerPresenter customerPresenter;

    @Transactional(readOnly = true)
    @Override
    public Optional<CustomerDto> findActiveById(UUID id) {
        return customerRepository.findActiveById(id)
                .map(customerPresenter::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CustomerDto> findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(customerPresenter::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CustomerDto> findByUsername(String username) {
        return customerRepository.findByUserUsername(username)
                .map(customerPresenter::toDto);
    }

    @Transactional
    @Override
    public Page<CustomerAllResponse> pageAll(Pageable paginacao) {
        return customerRepository.findAll(paginacao)
                .map(customerPresenter::toAllResponse);
    }
}
