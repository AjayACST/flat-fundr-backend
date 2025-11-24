package nz.co.flatfundr.api.service;

import nz.co.flatfundr.api.entity.UserAccount;
import nz.co.flatfundr.api.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OidcUserInfoService {
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public OidcUserInfoService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public OidcUserInfo loadUser(String username) {
        return new OidcUserInfo(findByEmail(username));
    }

    public Map<String, Object> findByEmail(String email) throws UsernameNotFoundException {
        UserAccount ua = userAccountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        return OidcUserInfo.builder()
                .subject(ua.getId().toString())
                .name(ua.getFirstName() + " " + ua.getLastName())
                .givenName(ua.getFirstName())
                .familyName(ua.getLastName())
                .email(ua.getEmail())
                .build()
                .getClaims();
    }
}
