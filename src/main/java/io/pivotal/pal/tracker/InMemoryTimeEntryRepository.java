package io.pivotal.pal.tracker;

import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> repo = new HashMap<>();

    private long nextId() { return repo.size()+1L; }

    @Override
    public TimeEntry create(TimeEntry any) {
        long next = nextId();
        repo.put(next, any);
        any.setId(next);
        return any;
    }

    @Override
    public TimeEntry find(long l) {
        return repo.get(l);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<TimeEntry>(repo.values());

    }

    @Override
    public TimeEntry update(long eq, TimeEntry any) {
        TimeEntry timeEntry = find(eq);
        if (timeEntry == null){
            return null;
        }
        delete(eq);
        any.setId(eq);
        repo.put(eq, any);
        return any;
    }

    @Override
    public void delete(long l) {
        TimeEntry timeEntry = repo.get(l);
        repo.remove(l);
    }
}
