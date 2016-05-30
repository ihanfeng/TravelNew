package com.travelogue.post.service;

import com.travelogue.post.domain.Post;
import com.travelogue.post.web.rest.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Post.
 */
public interface PostService {

    /**
     * Save a post.
     * 
     * @param postDTO the entity to save
     * @return the persisted entity
     */
    PostDTO save(PostDTO postDTO);

    /**
     *  Get all the posts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Post> findAll(Pageable pageable);

    /**
     *  Get the "id" post.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    PostDTO findOne(String id);

    /**
     *  Delete the "id" post.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);
}
