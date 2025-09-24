package backend.finance.api_user.infrastructure.gateways;

import backend.finance.api_user.application.dtos.internal.UserDto;
import backend.finance.api_user.infrastructure.ports.output.UserOutputPort;
import backend.finance.api_user.infrastructure.presenters.UserPresenter;
import backend.finance.api_user.infrastructure.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserGateway implements UserOutputPort {

    private final UserRepository userRepository;

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserPresenter::toUserDto);
    }
}
