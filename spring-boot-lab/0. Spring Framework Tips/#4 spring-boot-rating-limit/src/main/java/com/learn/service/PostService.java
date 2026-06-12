package com.learn.service;

import com.learn.model.Post;
import com.learn.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    // Constructor injection (consistent with PostController, easy to test).
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> posts() {
        log.info("Fetching all posts");
        return postRepository.findAll();
    }
}
