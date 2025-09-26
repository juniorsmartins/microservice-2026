package backend.finance.api_user.infrastructure.gateways;

import backend.finance.api_user.infrastructure.ports.output.CustomerDeleteOutputPort;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomerDeleteGateway implements CustomerDeleteOutputPort {

    private final CustomerRepository customerRepository;

    @Transactional
    @Override
    public void deleteById(UUID id) {
        customerRepository.deleteById(id);
    }
}
