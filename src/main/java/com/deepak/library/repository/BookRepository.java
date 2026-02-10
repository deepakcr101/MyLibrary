package com.deepak.library.repository;

import com.deepak.library.domain.Book;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface BookRepository extends Neo4jRepository<Book, Long> {
}
