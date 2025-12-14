package nz.co.flatfundr.api.service;

import nz.co.flatfundr.api.entity.UserAccount;
import nz.co.flatfundr.api.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OidcUserInfoService {
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public OidcUserInfoService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public Map<String, Object> loadUser(String username) {
        UserAccount ua = userAccountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));

        Map<String, Object> oidcUserInfo = OidcUserInfo.builder()
                .subject(ua.getId().toString())
                .name(ua.getFirstName() + " " + ua.getLastName())
                .givenName(ua.getFirstName())
                .familyName(ua.getLastName())
                .email(ua.getEmail())
                .build()
                .getClaims();
        Map<String, Object> userInfo = new HashMap<>(oidcUserInfo);
        userInfo.put("first_time_setup", ua.isFirstTimeSetup());
        if (ua.getLinkedFlat() != null) {
            userInfo.put("linked_flat", ua.getLinkedFlat().getId().toString());
        }
        if (ua.getOwnedFlat() == null) {
            userInfo.put("flat_owner", false);
        } else {
            userInfo.put("flat_owner", true);
        }
        return userInfo;
    }
}
