package com.courseproject.travelagencyrestapiawtentication;
import com.courseproject.travelagencyrestapiawtentication.secinit.models.User;
import com.courseproject.travelagencyrestapiawtentication.secinit.repository.UserRepository;
import com.courseproject.travelagencyrestapiawtentication.secinit.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    @Test
    public void testLoadUserByUsernameSuccess() {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("password");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }


    @Test
    public void testLoadUserByUsernameNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistentuser");
        });
        verify(userRepository, times(1)).findByUsername("nonexistentuser");
    }


    @Test
    public void testUpdateUsernameSuccess() {
        User mockUser = new User();
        mockUser.setUsername("oldUsername");
        when(userRepository.findByUsername("oldUsername")).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        userDetailsService.updateUsername("oldUsername", "newUsername");
        assertEquals("newUsername", mockUser.getUsername());
        verify(userRepository, times(1)).findByUsername("oldUsername");
        verify(userRepository, times(1)).save(mockUser);
    }


    @Test
    public void testUpdateUsernameUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            userDetailsService.updateUsername("oldUsername", "newUsername");
        });
        verify(userRepository, times(1)).findByUsername("oldUsername");
    }
}
