package gr.technico.technikon.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<T, K> {
    Optional<T> save(T t);
    Optional<T> findById(K id);
    List<T> findAll();
    boolean deleteById(K id);
    Optional<T> findByVat(String vat);
}
