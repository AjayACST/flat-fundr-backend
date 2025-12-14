package nz.co.flatfundr.api.schedule;

import nz.co.flatfundr.api.repository.FlatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AkahuImport {
    private final FlatRepository flatRepository;

    @Autowired
    AkahuImport(FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void startImport() {

    }

}
