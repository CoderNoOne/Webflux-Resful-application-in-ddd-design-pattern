package com.app.infrastructure.repository.impl;

import com.app.domain.movie.Movie;
import com.app.domain.movie.MovieRepository;
import com.app.infrastructure.repository.mongo.MongoMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepository {

    private final MongoMovieRepository mongoMovieRepository;

    @Override
    public Mono<Movie> addOrUpdate(Movie movie) {
        return mongoMovieRepository.save(movie);
    }

    @Override
    public Flux<Movie> addOrUpdateMany(List<Movie> movies) {
        return mongoMovieRepository.saveAll(movies);
    }

    @Override
    public Flux<Movie> findAll() {
        return mongoMovieRepository.findAll();
    }

    @Override
    public Mono<Movie> findById(String id) {
        return mongoMovieRepository.findById(id);
    }

    @Override
    public Flux<Movie> findAllById(List<String> ids) {
        return mongoMovieRepository.findAllById(ids);
    }

    @Override
    public Mono<Movie> deleteById(String id) {
        return mongoMovieRepository
                .findById(id)
                .flatMap(movie -> mongoMovieRepository
                        .delete(movie)
                        .then(Mono.just(movie)));
    }

    @Override
    public Mono<Movie> findByNameAndGenre(String name, String genre) {
        return mongoMovieRepository.findByNameAndGenre(name, genre);
    }

    @Override
    public Flux<Movie> deleteAllById(List<String> ids) {
        return mongoMovieRepository.findAllById(ids)
                .collectList()
                .flatMap(movies -> mongoMovieRepository.deleteAll(movies)
                        .then(Mono.just(movies)))
                .flatMapMany(Flux::fromIterable);
    }
}
