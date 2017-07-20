package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {


    private JdbcTimeEntryRepository jdbcTimeEntryRepository;
    private static final int MAX_TIME_ENTRIES = 5;

    public TimeEntryHealthIndicator(JdbcTimeEntryRepository jdbcTimeEntryRepository) {
        this.jdbcTimeEntryRepository = jdbcTimeEntryRepository;
    }

    @Override
    public Health health() {


        Health.Builder builder = new Health.Builder();


        if(jdbcTimeEntryRepository.list().size()< MAX_TIME_ENTRIES) {
            builder.up();
        } else {
            builder.down();
        }

        return builder.build();
    }
}
