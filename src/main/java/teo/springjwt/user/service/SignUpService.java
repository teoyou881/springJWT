package teo.springjwt.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import teo.springjwt.user.UserEntity;
import teo.springjwt.user.UserRole;
import teo.springjwt.user.dto.RegisterDTO;
import teo.springjwt.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SignUpService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder;

  public void signUpProcess(RegisterDTO registerDTO) {
    Boolean isExist = userRepository.existsByEmail(registerDTO.getEmail());

    if (isExist) {
      return;
    }

    UserEntity user = new UserEntity(registerDTO.getEmail(),encoder.encode(registerDTO.getPassword()),UserRole.ROLE_USER);
    userRepository.save(user);
  }
}
