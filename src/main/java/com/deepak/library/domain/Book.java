package com.deepak.library.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
@Data
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue
    private Long id;
    private String title;

    @Relationship(type = "WRITTEN_BY", direction = Relationship.Direction.OUTGOING)
    private Author author;

    public Book(String title) {
        this.title = title;
    }
}
