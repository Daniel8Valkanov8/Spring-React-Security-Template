package com.courseproject.travelagencyrestapiawtentication.secinit.security.services;

import com.courseproject.travelagencyrestapiawtentication.secinit.models.User;
import com.courseproject.travelagencyrestapiawtentication.secinit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

  public void updateUsername(String currentUsername, String newUsername) {
    User user = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setUsername(newUsername);
    userRepository.save(user);
  }


}
