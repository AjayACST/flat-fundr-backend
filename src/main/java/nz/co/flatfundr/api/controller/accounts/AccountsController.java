package nz.co.flatfundr.api.controller.accounts;

import nz.co.flatfundr.api.repository.AccountRepository;
import nz.co.flatfundr.api.repository.FlatRepository;
import nz.co.flatfundr.api.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
    private final UserAccountRepository userAccountRepository;
    private final FlatRepository flatRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public AccountsController(UserAccountRepository userAccountRepository, FlatRepository flatRepository, AccountRepository accountRepository) {
        this.userAccountRepository = userAccountRepository;
        this.flatRepository = flatRepository;
        this.accountRepository = accountRepository;
    }


    @GetMapping("/link")
    public
}
