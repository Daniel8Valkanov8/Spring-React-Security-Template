package com.courseproject.travelagencyrestapiawtentication.secinit.controllers;

import com.courseproject.travelagencyrestapiawtentication.secinit.models.User;

import com.courseproject.travelagencyrestapiawtentication.secinit.models.dto.request.UpdateUsernameRequest;
import com.courseproject.travelagencyrestapiawtentication.secinit.payload.response.MessageResponse;
import com.courseproject.travelagencyrestapiawtentication.secinit.repository.UserRepository;
import com.courseproject.travelagencyrestapiawtentication.secinit.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UpdateUserNameController {
    @Autowired
    private UserRepository userRepository;
    @PreAuthorize("hasRole('USER')" + " || hasRole('ADMIN')")
    @PutMapping("/update-username")
    public ResponseEntity<?> updateUsername(@Valid @RequestBody UpdateUsernameRequest updateUsernameRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));
        user.setUsername(updateUsernameRequest.getNewUsername());
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Username updated successfully!"));
    }
}
