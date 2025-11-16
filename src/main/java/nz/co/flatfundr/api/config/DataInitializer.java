//package nz.co.flatfundr.api.config;
//
//import nz.co.flatfundr.api.entity.UserAccount;
//import nz.co.flatfundr.api.repository.UserAccountRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Set;
//
//@Configuration
//public class DataInitializer {
//
//    @Bean
//    public CommandLineRunner seedUsers(UserAccountRepository userRepo, PasswordEncoder passwordEncoder) {
//        return args -> {
//            if (userRepo.findByUsername("admin").isEmpty()) {
//                UserAccount admin = new UserAccount("admin", passwordEncoder.encode("adminpass"), Set.of("ROLE_ADMIN"));
//                userRepo.save(admin);
//            }
//        };
//    }
//}
//
