package nz.co.flatfundr.api.config;

import nz.co.flatfundr.api.entity.Flat;
import nz.co.flatfundr.api.entity.UserAccount;
import nz.co.flatfundr.api.repository.FlatRepository;
import nz.co.flatfundr.api.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedUsers(UserAccountRepository userRepo, PasswordEncoder passwordEncoder, FlatRepository flatRepo) {
        return _ -> {
            if (userRepo.findByEmail("ajay@quirky.codes").isEmpty()) {
                UserAccount admin = new UserAccount("ajay@quirky.codes", passwordEncoder.encode("adminpass"), Set.of("ROLE_ADMIN"), "Ajay", "Quirk", null);
                userRepo.save(admin);
            }
        };
    }
}

