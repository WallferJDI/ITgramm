package com.wallferjdi.itgramm.service;

import com.wallferjdi.itgramm.entity.ImageModel;
import com.wallferjdi.itgramm.entity.Post;
import com.wallferjdi.itgramm.entity.User;
import com.wallferjdi.itgramm.repository.ImageRepository;
import com.wallferjdi.itgramm.repository.PostRepository;
import com.wallferjdi.itgramm.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageService {

    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ImageService(ImageRepository imageRepository, UserRepository userRepository, PostRepository postRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public ImageModel uploadImageToUser(MultipartFile file,Principal principal) throws IOException {
        User user = getUserFromPrincipal(principal);
        ImageModel userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if(!ObjectUtils.isEmpty(userProfileImage)){
            imageRepository.delete(userProfileImage);
        }

        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageRepository.save(imageModel);
    }
    public ImageModel uploadImageToPost(MultipartFile file,Principal principal, Long postId) throws IOException {
        User user = getUserFromPrincipal(principal);
        Post post = user.getPosts()
                .stream().filter(p->p.getId().equals(postId))
                .collect(toSinglePostCollector());
        ImageModel imageModel = new ImageModel();
        imageModel.setPostId(postId);
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());

        return imageRepository.save(imageModel);
    }

    public ImageModel getImageToUser(Principal principal){
        User user = getUserFromPrincipal(principal);
        ImageModel imageModel = imageRepository.findByUserId(user.getId()).orElse(null);

        if(!ObjectUtils.isEmpty(imageModel)){
            imageModel.setImageBytes(deCompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    public ImageModel getImageToPost(Long postId){
        ImageModel imageModel = imageRepository.findByPostId(postId).orElseThrow();

        if(!ObjectUtils.isEmpty(imageModel)){
            imageModel.setImageBytes(deCompressBytes(imageModel.getImageBytes()));
        }

        return imageModel;
    }

    private byte[] compressBytes(byte[] data){
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()){
            int count = deflater.deflate(buffer);
            outputStream.write(buffer,0,count);
        }
        try {
            outputStream.close();
        }catch (Exception e){
            LOG.error("Can not compress the bytes");
        }
        return outputStream.toByteArray();
    }

    private byte[] deCompressBytes(byte[] data){
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()){
                int count = inflater.inflate(buffer);
                outputStream.write(buffer,0,count);
            }
            outputStream.close();
        }catch (Exception e){
            LOG.error("Can not compress the bytes");
        }

        return outputStream.toByteArray();
    }

    private <T>Collector<T,?,T>toSinglePostCollector(){
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list->{
                    if (list.size()!=1){
                        throw new IllegalStateException();
                    }
                    return  list.get(0);
                }
        );
    }


    public User getUserFromPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(()-> new UsernameNotFoundException(" User with" +
                "this username not exist "+ username));
    }
}
