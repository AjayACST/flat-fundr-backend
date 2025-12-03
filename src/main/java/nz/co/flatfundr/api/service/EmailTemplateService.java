package nz.co.flatfundr.api.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
public class EmailTemplateService {
    private final SpringTemplateEngine templateEngine;

    public EmailTemplateService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Renders a Thymeleaf template from /templates/email/*.html with the provided variables.
     *
     * @param templateName name relative to templates folder (eg: "email/invite")
     * @param variables map of variables to expose to the template
     * @return rendered HTML string
     */
    public String render(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        if (variables != null) context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
}

