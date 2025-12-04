package nz.co.flatfundr.api.controller.flat;

import jakarta.validation.Valid;
import nz.co.flatfundr.api.dto.flat.CreateNewFlatRequest;
import nz.co.flatfundr.api.entity.Flat;
import nz.co.flatfundr.api.entity.FlatJoin;
import nz.co.flatfundr.api.entity.UserAccount;
import nz.co.flatfundr.api.repository.FlatJoinRepository;
import nz.co.flatfundr.api.repository.FlatRepository;
import nz.co.flatfundr.api.repository.UserAccountRepository;
import nz.co.flatfundr.api.service.CodeGeneratorService;
import nz.co.flatfundr.api.service.EmailTemplateService;
import nz.co.flatfundr.api.service.UseSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/flat")
public class FlatController {
    private final UserAccountRepository userAccountRepository;
    private final FlatRepository flatRepository;
    private final EmailTemplateService emailTemplateService;
    private final FlatJoinRepository flatJoinRepository;
    private final UseSendService emailService;

    @Autowired
    public FlatController(
            UserAccountRepository userAccountRepository,
            FlatRepository flatRepository,
            EmailTemplateService emailTemplateService,
            FlatJoinRepository flatJoinRepository,
            UseSendService emailService
    ) {
        this.userAccountRepository = userAccountRepository;
        this.flatRepository = flatRepository;
        this.emailTemplateService = emailTemplateService;
        this.flatJoinRepository = flatJoinRepository;
        this.emailService = emailService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createFlat(@Valid @RequestBody CreateNewFlatRequest payload, @AuthenticationPrincipal Jwt jwt) {
        // check user is not already part of flat
        UserAccount user = userAccountRepository.findByEmail(jwt.getSubject()).orElseThrow(()->new UsernameNotFoundException(jwt.getSubject()));
        if (user.getLinkedFlat() != null) {
            return ResponseEntity.badRequest().body("Your Account is already linked to a flat!");
        }
        Flat flat = new Flat(payload.flatName());
        flatRepository.save(flat);
        user.setLinkedFlat(flat);
        userAccountRepository.save(user);
        if (payload.flatmateEmailList() != null) {
            for (String e : payload.flatmateEmailList()) {
                String code;
                try {
                    code = CodeGeneratorService.generateCode(e);
                } catch (NoSuchAlgorithmException _) {
                    return ResponseEntity.internalServerError().body("An internal server error has occurred!");
                }
                FlatJoin join = new FlatJoin(e, flat, code);
                Map<String, Object> context = new HashMap<>();
                context.put("flatName", payload.flatName());
                context.put("code", code);
                String htmlContent = emailTemplateService.render("email/invite", context);
                emailService.sendHtmlMail(htmlContent, e, "You've been invited to join a flat on FlatFundr!");
                flatJoinRepository.save(join);
            }
        }
        return ResponseEntity.ok(flat);
    }
}
