package com.travelogue.post.web.rest;

import com.travelogue.post.TraveloguePostApp;
import com.travelogue.post.domain.Post;
import com.travelogue.post.repository.PostRepository;
import com.travelogue.post.service.PostService;
import com.travelogue.post.web.rest.dto.PostDTO;
import com.travelogue.post.web.rest.mapper.PostMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PostResource REST controller.
 *
 * @see PostResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TraveloguePostApp.class)
@WebAppConfiguration
@IntegrationTest
public class PostResourceIntTest {

    private static final String DEFAULT_HEAD = "AAAAA";
    private static final String UPDATED_HEAD = "BBBBB";
    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";
    private static final String DEFAULT_USERS = "AAAAA";
    private static final String UPDATED_USERS = "BBBBB";
    private static final String DEFAULT_PLACES = "AAAAA";
    private static final String UPDATED_PLACES = "BBBBB";
    private static final String DEFAULT_RESOURCES = "AAAAA";
    private static final String UPDATED_RESOURCES = "BBBBB";

    @Inject
    private PostRepository postRepository;

    @Inject
    private PostMapper postMapper;

    @Inject
    private PostService postService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPostMockMvc;

    private Post post;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PostResource postResource = new PostResource();
        ReflectionTestUtils.setField(postResource, "postService", postService);
        ReflectionTestUtils.setField(postResource, "postMapper", postMapper);
        this.restPostMockMvc = MockMvcBuilders.standaloneSetup(postResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        postRepository.deleteAll();
        post = new Post();
        post.setHead(DEFAULT_HEAD);
        post.setContent(DEFAULT_CONTENT);
        post.setUsers(DEFAULT_USERS);
        post.setPlaces(DEFAULT_PLACES);
        post.setResources(DEFAULT_RESOURCES);
    }

    @Test
    public void createPost() throws Exception {
        int databaseSizeBeforeCreate = postRepository.findAll().size();

        // Create the Post
        PostDTO postDTO = postMapper.postToPostDTO(post);

        restPostMockMvc.perform(post("/api/posts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postDTO)))
                .andExpect(status().isCreated());

        // Validate the Post in the database
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(databaseSizeBeforeCreate + 1);
        Post testPost = posts.get(posts.size() - 1);
        assertThat(testPost.getHead()).isEqualTo(DEFAULT_HEAD);
        assertThat(testPost.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testPost.getUsers()).isEqualTo(DEFAULT_USERS);
        assertThat(testPost.getPlaces()).isEqualTo(DEFAULT_PLACES);
        assertThat(testPost.getResources()).isEqualTo(DEFAULT_RESOURCES);
    }

    @Test
    public void checkHeadIsRequired() throws Exception {
        int databaseSizeBeforeTest = postRepository.findAll().size();
        // set the field null
        post.setHead(null);

        // Create the Post, which fails.
        PostDTO postDTO = postMapper.postToPostDTO(post);

        restPostMockMvc.perform(post("/api/posts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postDTO)))
                .andExpect(status().isBadRequest());

        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = postRepository.findAll().size();
        // set the field null
        post.setContent(null);

        // Create the Post, which fails.
        PostDTO postDTO = postMapper.postToPostDTO(post);

        restPostMockMvc.perform(post("/api/posts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postDTO)))
                .andExpect(status().isBadRequest());

        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllPosts() throws Exception {
        // Initialize the database
        postRepository.save(post);

        // Get all the posts
        restPostMockMvc.perform(get("/api/posts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId())))
                .andExpect(jsonPath("$.[*].head").value(hasItem(DEFAULT_HEAD.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].users").value(hasItem(DEFAULT_USERS.toString())))
                .andExpect(jsonPath("$.[*].places").value(hasItem(DEFAULT_PLACES.toString())))
                .andExpect(jsonPath("$.[*].resources").value(hasItem(DEFAULT_RESOURCES.toString())));
    }

    @Test
    public void getPost() throws Exception {
        // Initialize the database
        postRepository.save(post);

        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", post.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(post.getId()))
            .andExpect(jsonPath("$.head").value(DEFAULT_HEAD.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.users").value(DEFAULT_USERS.toString()))
            .andExpect(jsonPath("$.places").value(DEFAULT_PLACES.toString()))
            .andExpect(jsonPath("$.resources").value(DEFAULT_RESOURCES.toString()));
    }

    @Test
    public void getNonExistingPost() throws Exception {
        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePost() throws Exception {
        // Initialize the database
        postRepository.save(post);
        int databaseSizeBeforeUpdate = postRepository.findAll().size();

        // Update the post
        Post updatedPost = new Post();
        updatedPost.setId(post.getId());
        updatedPost.setHead(UPDATED_HEAD);
        updatedPost.setContent(UPDATED_CONTENT);
        updatedPost.setUsers(UPDATED_USERS);
        updatedPost.setPlaces(UPDATED_PLACES);
        updatedPost.setResources(UPDATED_RESOURCES);
        PostDTO postDTO = postMapper.postToPostDTO(updatedPost);

        restPostMockMvc.perform(put("/api/posts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postDTO)))
                .andExpect(status().isOk());

        // Validate the Post in the database
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(databaseSizeBeforeUpdate);
        Post testPost = posts.get(posts.size() - 1);
        assertThat(testPost.getHead()).isEqualTo(UPDATED_HEAD);
        assertThat(testPost.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testPost.getUsers()).isEqualTo(UPDATED_USERS);
        assertThat(testPost.getPlaces()).isEqualTo(UPDATED_PLACES);
        assertThat(testPost.getResources()).isEqualTo(UPDATED_RESOURCES);
    }

    @Test
    public void deletePost() throws Exception {
        // Initialize the database
        postRepository.save(post);
        int databaseSizeBeforeDelete = postRepository.findAll().size();

        // Get the post
        restPostMockMvc.perform(delete("/api/posts/{id}", post.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
