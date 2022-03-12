package pl.lotto.resultchecker;

import org.springframework.data.mongodb.repository.MongoRepository;

interface WinnerRepository extends MongoRepository<Winner, String> {
}
