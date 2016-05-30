package com.travelogue.post.repository;

import com.travelogue.post.domain.Post;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Post entity.
 */
@SuppressWarnings("unused")
public interface PostRepository extends MongoRepository<Post,String> {

}
