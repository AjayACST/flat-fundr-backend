package nz.co.flatfundr.api.controller.accounts;

import nz.co.flatfundr.api.repository.AccountRepository;
import nz.co.flatfundr.api.repository.FlatRepository;
import nz.co.flatfundr.api.repository.UserAccountRepository;
import nz.co.flatfundr.api.service.akahu.AkahuResponseError;
import nz.co.flatfundr.api.service.akahu.AkahuService;
import nz.co.flatfundr.api.service.akahu.model.AkahuAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
    private final UserAccountRepository userAccountRepository;
    private final FlatRepository flatRepository;
    private final AccountRepository accountRepository;
    private final AkahuService akahuService;

    @Autowired
    public AccountsController(UserAccountRepository userAccountRepository, FlatRepository flatRepository, AccountRepository accountRepository, AkahuService akahuService) {
        this.userAccountRepository = userAccountRepository;
        this.flatRepository = flatRepository;
        this.accountRepository = accountRepository;
        this.akahuService = akahuService;
    }


    @GetMapping("/link/new")
    public ResponseEntity<?> accountsForLinking() {
        List<AkahuAccount> akahuAccounts;
        try {
            akahuAccounts = akahuService.getAllAccounts();
        } catch (AkahuResponseError _) {
            return ResponseEntity.internalServerError().body("Something went wrong while fetching accounts from Akahu.");
        }
        List<AkahuAccount> returnAccounts = new ArrayList<>();
        for (AkahuAccount account : akahuAccounts) {
            if (!accountRepository.existsAccountByAkahuId(account.getId())) {
                returnAccounts.add(account);
            }
        }

        return ResponseEntity.ok(returnAccounts);
    }
}
