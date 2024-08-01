package gr.technico.technikon.repositories;

import java.util.Optional;

public interface Repository<T, K> {
    Optional<T> save(T t);
}
