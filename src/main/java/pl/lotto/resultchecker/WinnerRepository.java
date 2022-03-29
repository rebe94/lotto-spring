package pl.lotto.resultchecker;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WinnerRepository extends MongoRepository<Winner, String> {

    Optional<Winner> findByHash(String hash);
}
