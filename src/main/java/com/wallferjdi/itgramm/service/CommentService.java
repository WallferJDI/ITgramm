package com.wallferjdi.itgramm.service;

import com.wallferjdi.itgramm.dto.CommentDTO;
import com.wallferjdi.itgramm.entity.Comment;
import com.wallferjdi.itgramm.entity.Post;
import com.wallferjdi.itgramm.entity.User;
import com.wallferjdi.itgramm.exception.PostNotFoundException;
import com.wallferjdi.itgramm.repository.CommentRepository;
import com.wallferjdi.itgramm.repository.PostRepository;
import com.wallferjdi.itgramm.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    public static final Logger LOG = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment saveComment(Long postId,CommentDTO commentDTO, Principal principal){

        User user = getUserFromPrincipal(principal);
        Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException("Post not found"));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());
        LOG.info("Saving comment "+ postId);
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsFromPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException("Post not found"));
        List<Comment> comments = commentRepository.findAllByPost(post);
        return comments;
    }
    public void deleteComment(Long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    public User getUserFromPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(()-> new UsernameNotFoundException(" User with" +
                "this username not exist "+ username));
    }

}
