package pl.lotto.resultchecker;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

class InMemoryWinnerRepository implements WinnerRepository {

    private final List<Winner> winners = new ArrayList<>();

    @Override
    public Winner save(Winner winner) {
        return winner;
    }

    @Override
    public <S extends Winner> List<S> saveAll(Iterable<S> entities) {
        entities.forEach(e -> winners.add(e));
        return (List<S>) winners;
    }

    @Override
    public <S extends Winner> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Winner> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Winner> findAll() {
        return winners;
    }

    @Override
    public Iterable<Winner> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Winner entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Winner> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Winner> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Winner> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Winner> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Winner> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Winner> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Winner> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Winner> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Winner> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Winner> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Winner, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
