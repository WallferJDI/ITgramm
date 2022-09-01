package com.wallferjdi.itgramm.service;

import com.wallferjdi.itgramm.dto.PostDTO;
import com.wallferjdi.itgramm.entity.Post;
import com.wallferjdi.itgramm.entity.User;
import com.wallferjdi.itgramm.exception.PostNotFoundException;
import com.wallferjdi.itgramm.repository.ImageRepository;
import com.wallferjdi.itgramm.repository.PostRepository;
import com.wallferjdi.itgramm.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal){
        User user = getUserFromPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
                LOG.info("saving post ");
        return postRepository.save(post);
    }

    public List<Post> getAllPosts(){
        return postRepository.findAllByOrderByCreatedTimeDesc();
    }

    public User getUserFromPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(()-> new UsernameNotFoundException(" User with" +
                "this username not exist "+ username));
    }


    public List<Post> getAllPostForUser(Principal principal){
        User user = getUserFromPrincipal(principal);
        return postRepository.findAllByUserOrderByCreatedTimeDesc(user);
    }

    public Post getPostById(Long postId,Principal principal){
        User user = getUserFromPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId,user).orElseThrow(()->new PostNotFoundException("Post not found"));
    }
}
