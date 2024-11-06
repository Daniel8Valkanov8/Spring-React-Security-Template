package com.courseproject.travelagencyrestapiawtentication.secinit;

import com.courseproject.travelagencyrestapiawtentication.secinit.models.ERole;
import com.courseproject.travelagencyrestapiawtentication.secinit.models.Role;
import com.courseproject.travelagencyrestapiawtentication.secinit.models.User;
import com.courseproject.travelagencyrestapiawtentication.secinit.repository.RoleRepository;
import com.courseproject.travelagencyrestapiawtentication.secinit.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class InitializeUserRoles {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public InitializeUserRoles(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initializeUserRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.ROLE_USER));
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }
    }

    public void commandForAuthorizationAdmin() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter command:");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("addadmin")) {
                System.out.println("Enter the username to authorize as admin:");
                String username = scanner.nextLine();

                authorizeUserAsAdmin(username);
            } else if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Unknown command. Please try again.");
            }
        }

        scanner.close();
    }

    private void authorizeUserAsAdmin(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            // Зареждаме ролите на потребителя
            User existingUser = user.get();
            existingUser.getRoles().size(); // Зареждане на роли
            existingUser.getRoles().add(roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() ->
                    new RuntimeException("Admin role not found")));
            userRepository.save(existingUser);
            System.out.println("User " + username + " has been authorized as admin.");
        } else {
            System.out.println("User not found.");
        }
    }
}
