package gr.technico.technikon.repositories;

import java.util.List;
import java.util.Optional;

/**
 *
 * @param <T> the model class
 * @param <K> the key class
 */
public interface Repository<T,K> {
      Optional<T> findById(K id) ;
      List<T> findAll() ;
      Optional<T> save(T t) ;
      boolean deleteById(K id);
}
