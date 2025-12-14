package nz.co.flatfundr.api.repository;

import nz.co.flatfundr.api.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
