package pl.lotto.resultchecker;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoWinnerRepository
        extends WinnerRepository, MongoRepository<Winner, String> {
}
