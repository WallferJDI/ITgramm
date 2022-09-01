package com.wallferjdi.itgramm.repository;

import com.wallferjdi.itgramm.entity.Post;
import com.wallferjdi.itgramm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByUserOrderByCreatedTimeDesc(User user);
    List<Post> findAllByOrderByCreatedTimeDesc();
    Optional<Post> findPostByIdAndUser(Long id,User user);
}
