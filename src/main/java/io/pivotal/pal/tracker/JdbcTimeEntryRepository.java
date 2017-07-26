package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;



    public JdbcTimeEntryRepository(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);


    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                            "VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );
            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());

            return statement;
        }, generatedKeyHolder);

        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long l) {

        return new TimeEntry(jdbcTemplate.queryForMap("Select * from time_entries where id = ?", l));
    }

    @Override
    public List<TimeEntry> list() {

        List<Map<String,Object>> listMapTimeEntries =jdbcTemplate.queryForList("Select * from time_entries");

        List<TimeEntry> listTimeEntries = new ArrayList<TimeEntry>();

        listMapTimeEntries.forEach(m -> listTimeEntries.add(new TimeEntry(m)));

        return listTimeEntries;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {


        jdbcTemplate.update("UPDATE time_entries " +
                        "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                        "WHERE id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                id);

        return find(id);
    }

    @Override
    public void delete(long id) {

        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", id);
    }
}
