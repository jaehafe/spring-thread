package com.spring.board.service;

import com.spring.board.exception.follow.FollowAlreadyExistsException;
import com.spring.board.exception.follow.FollowNotFoundException;
import com.spring.board.exception.follow.InvalidFollowingException;
import com.spring.board.exception.user.UserAlreadyExistsException;
import com.spring.board.exception.user.UserNotFoundException;
import com.spring.board.model.entity.FollowEntity;
import com.spring.board.model.entity.UserEntity;
import com.spring.board.model.user.User;
import com.spring.board.model.user.UserAuthenticationResponse;
import com.spring.board.model.user.UserPatchRequestBody;
import com.spring.board.repository.FollowEntityRepository;
import com.spring.board.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;
    private final FollowEntityRepository followEntityRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserEntityRepository userEntityRepository, FollowEntityRepository followEntityRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userEntityRepository = userEntityRepository;
        this.followEntityRepository = followEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public User signUp(String username, String password) {
        userEntityRepository
                .findByUsername(username)
                .ifPresent(
                        user -> {
                            throw new UserAlreadyExistsException();
                        });

        var userEntity = UserEntity.of(username, passwordEncoder.encode(password));
        userEntityRepository.save(userEntity);

        return User.from(userEntity);
    }

    public UserAuthenticationResponse authenticate(String username, String password) {
        var userEntity =
                userEntityRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            var accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new UserNotFoundException();
        }
    }

    public List<User> getUsers(String query) {

        List<UserEntity> userEntities;

        if(query != null && !query.isEmpty()) {
            userEntities = userEntityRepository.findByUsernameContaining(query);
        } else {
            userEntities = userEntityRepository.findAll();
        }

        return userEntities.stream().map(User::from).toList();
    }

    public User getUser(String username) {
        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return User.from(userEntity);
    }

    public User updateUser(
            String username,
            UserPatchRequestBody requestBody,
            UserEntity currentUser
    ) {

        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if(!userEntity.equals(currentUser)) {
            throw new UserNotFoundException(username);
        }

        if(requestBody.description() != null) {
            userEntity.setDescription(requestBody.description());
        }

        return User.from(userEntityRepository.save(userEntity));
    }

    @Transactional
    public User follow(String username, UserEntity currentUser) {

        UserEntity following = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if(following.equals(currentUser)) {
            throw new InvalidFollowingException("A user cannot follow themselves.");
        }

        followEntityRepository
                .findByFollowerAndFollowing(currentUser, following)
                .ifPresent(
                        follow -> {
                            throw new FollowAlreadyExistsException(currentUser, following);
                        });
        followEntityRepository.save(FollowEntity.of(currentUser, following));

        following.setFollowersCount(following.getFollowersCount() + 1);
        currentUser.setFollowingsCount(currentUser.getFollowingsCount() + 1);

//        userEntityRepository.save(following);
//        userEntityRepository.save(currentUser);
        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following);
    }

    @Transactional
    public User unFollow(String username, UserEntity currentUser) {

        UserEntity following = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if(following.equals(currentUser)) {
            throw new InvalidFollowingException("A user cannot unfollow themselves.");
        }

        FollowEntity followEntity =
                followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .orElseThrow(() -> new FollowNotFoundException(currentUser, following));

        followEntityRepository.delete(followEntity);

        following.setFollowersCount(following.getFollowersCount() - 1);
        currentUser.setFollowingsCount(Math.max(0, currentUser.getFollowingsCount() - 1));

        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following);
    }

    public List<User> getFollowersByUsername(String username) {
        var following =
                userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        var followEntities = followEntityRepository.findByFollowing(following);
        return followEntities.stream().map(follow -> User.from(follow.getFollower())).toList();
    }

    public List<User> getFollowingsByUsername(String username) {
        var follower =
                userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        var followEntities = followEntityRepository.findByFollower(follower);
        return followEntities.stream().map(follow -> User.from(follow.getFollowing())).toList();
    }
}
