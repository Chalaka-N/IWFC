package com.iwfc.repository;

import com.iwfc.exception.DuplicateDataException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GenericRepository<T> {
    private final Map<String, T> data = new HashMap<>();

    public void add(String id, T value) throws DuplicateDataException {
        if (data.containsKey(id)) {
            throw new DuplicateDataException("Duplicate ID: " + id);
        }
        data.put(id, value);
    }

    public Optional<T> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public boolean contains(String id) { return data.containsKey(id); }

    public void remove(String id) { data.remove(id); }
}
