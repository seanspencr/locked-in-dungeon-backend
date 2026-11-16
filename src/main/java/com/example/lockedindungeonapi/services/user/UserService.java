package com.example.lockedindungeonapi.services.user;

import com.example.lockedindungeonapi.dto.auth.SignupRequest;
import com.example.lockedindungeonapi.dto.auth.SignupResponse;
import com.example.lockedindungeonapi.dto.settings.ModifySettingsRequest;
import com.example.lockedindungeonapi.dto.user.UserDto;
import com.example.lockedindungeonapi.exceptions.user.UserAlreadyExistException;
import com.example.lockedindungeonapi.exceptions.user.UserNotFoundException;
import com.example.lockedindungeonapi.model.Authority;
import com.example.lockedindungeonapi.model.FirebaseEntity;
import com.example.lockedindungeonapi.model.User;
import com.example.lockedindungeonapi.model.UserSettings;
import com.example.lockedindungeonapi.repository.AuthorityRepository;
import com.example.lockedindungeonapi.repository.FirebaseEntityRepository;
import com.example.lockedindungeonapi.repository.UserRepository;
import com.example.lockedindungeonapi.repository.UserSettingsRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.*;

@Service
public class UserService  {

    UserRepository userRepo;
    PasswordEncoder encoder;
    AuthorityRepository authorityRepository;
    FirebaseEntityRepository firebaseEntityRepository;
    UserSettingsRepository userSettingsRepo;

    public UserService(UserRepository userRepo, PasswordEncoder encoder, AuthorityRepository authorityRepository, FirebaseEntityRepository firebaseEntityRepository, UserSettingsRepository userSettingRepo) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.authorityRepository = authorityRepository;
        this.firebaseEntityRepository = firebaseEntityRepository;
        this.userSettingsRepo = userSettingRepo;
    }

    public User findUserByUsername(String username){
        var user = userRepo.findByUsername(username);
        return user.orElseThrow(() -> new UserNotFoundException("User is not found"));
    }

    private Authority findOrCreateAuthority(String name){
        return authorityRepository.findByName(name).orElseGet(
                ()-> {
                    Authority newAuth = new Authority();
                    newAuth.setName(name);
                    Authority saved = authorityRepository.save(newAuth);
                    return  saved;
                }
        );
    }

    public SignupResponse createUser(SignupRequest req) {

        var existing = userRepo.findByUsername(req.username());
        if(existing.isPresent()){
          throw new UserAlreadyExistException("User already exist");
        }

//        user.setPassword(req.password());

        User user = new User();
        user.setPassword(encoder.encode(req.password()));
        user.setUsername(req.username());
        Set<Authority> auth = new HashSet<>();
        auth.add(findOrCreateAuthority(Authority.ofUser().getName()));
        user.setAuthorities(auth);
        // settings default ikut dibuat, biar tiap user selalu punya row user_settings
        user.setSettings(new UserSettings(user));

        try {
            var inserted = userRepo.save(user);
            return new SignupResponse(
                    UserDto.fromUser(inserted),
                    "Success"
            );
        } catch (TransactionSystemException e) {
            Throwable rootCause = e.getRootCause();
            // Cek apakah rootCause adalah ConstraintViolationException (validasi)
            if (rootCause instanceof ConstraintViolationException) {
                // Tampilkan pesan validasi yang gagal
            }
            // Atau cek pesan error database lainnya
            throw( new RuntimeException(Arrays.toString(e.getStackTrace())));
        }


    }

    public List<User> findAll() {
        return userRepo.findAll();
    }


    public Optional<User> findByRefreshToken(String refreshToken) {
        return userRepo.findByRefreshToken(refreshToken);
    }

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public FirebaseEntity registerFirebase(Long userId, String fid, String devicename) {

        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found, cant create firebase entity"));


        FirebaseEntity entity = new FirebaseEntity();
        entity.setFid(fid);
        entity.setDeviceName(devicename);
        entity.setUser(user);
        return this.firebaseEntityRepository.save(entity);
    }

    public UserSettings getUserSettings(Long id) {

        return userSettingsRepo.findByUserId(id).orElse(new UserSettings());
    }

    public UserSettings modifySettings(Long userId, ModifySettingsRequest req) {
        var settings = userSettingsRepo.findByUserId(userId).orElse(new UserSettings(userRepo.getReferenceById(userId)));


        req.showStats().ifPresent(settings::setShowStats);
        req.showBlockingList().ifPresent(settings::setShowBlocklist);


        return userSettingsRepo.save(settings);
    }
}
