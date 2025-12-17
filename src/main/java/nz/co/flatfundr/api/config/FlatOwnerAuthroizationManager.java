package nz.co.flatfundr.api.config;

import nz.co.flatfundr.api.entity.UserAccount;
import nz.co.flatfundr.api.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Supplier;

@Component
public class FlatOwnerAuthroizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public FlatOwnerAuthroizationManager(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationDecision decision = this.check(authentication, object);
        if (decision != null && !decision.isGranted()) {
            throw new AuthorizationDeniedException("Access Denied", decision);
        }
    }

    @Nullable
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        Authentication authentication = authenticationSupplier.get();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }
        String email = authentication.getName();
        boolean isOwner = userAccountRepository.findByEmail(email)
                .map(UserAccount::getOwnedFlat)
                .isPresent();
        System.out.println(isOwner);
        return new AuthorizationDecision(isOwner);
    }

    @Nullable
    @Override
    public AuthorizationResult authorize(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        return this.check(authentication, object);
    }
}
