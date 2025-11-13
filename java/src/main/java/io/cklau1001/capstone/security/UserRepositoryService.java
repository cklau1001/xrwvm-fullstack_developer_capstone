package io.cklau1001.capstone.security;

import io.cklau1001.capstone.model.AppRole;
import io.cklau1001.capstone.model.AppUser;
import io.cklau1001.capstone.repository.AppRoleRepository;
import io.cklau1001.capstone.repository.AppUserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserRepositoryService {

    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public boolean userExists(String username) {

        Optional<AppUser> optionalAppUser = appUserRepository.findById(username);

        return optionalAppUser.isPresent();
    }

    public UserDetails loadUserByusername(String username) {

        Optional<AppUser> optionalAppUser = appUserRepository.findById(username);
        AppUser appUser = optionalAppUser.orElseThrow(() -> new UsernameNotFoundException("User not found, username=" + username));

        List<AppRole> appRoleList = appRoleRepository.findByAppRoleIdUsername(username);
        log.debug("[loadUserByusername]: optionalAppUser={}", optionalAppUser);

        List<SimpleGrantedAuthority> roleList = appRoleList.stream()
                .map(r -> new SimpleGrantedAuthority(r.getRole())).toList();

        roleList.forEach(e -> log.debug("role:" + e.toString()));

        UserDetails userDetails = User.withUsername(appUser.getUsername())
                .password(passwordEncoder.encode(appUser.getPassword()))
                .authorities(roleList)
                .build();

        return userDetails;
    }

    public void createUser(AppUser appUser) {

        log.info("[createUser]: entered: appUser={}", appUser);
        appUserRepository.save(appUser);

    }

    public void deleteUser(String username) {

        log.info("[deleteUser]: entered: username={}", username);

        if (userExists(username)) {
            appUserRepository.deleteById(username);
            log.info("[deleteUser]: entered: username={} deleted", username);
        } else {
            log.info("[deleteUser]: entered: username={} not found - SKIP", username);
        }
    }

}
