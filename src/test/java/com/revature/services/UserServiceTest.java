package com.revature.services;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private static UserService userService;
    private static UserRepository userRepository;

    private User GENERIC_EMPLOYEE_1;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        userService = new UserService();
        userRepository = mock(UserRepository.class);
    }

    @Before
    public void setUp() throws Exception {
        GENERIC_EMPLOYEE_1 = new User(1, "genericEmployee1", "genericPassword", Role.EMPLOYEE);
    }

    @Test
    public void testGetByUsernamePassesWhenUsernameExists() {
        when(userRepository.getByUsername("genericEmployee1")).thenReturn(Optional.of(GENERIC_EMPLOYEE_1));
    }

    @Test
    public void testGetByUsernamePassesWhenUsernameNotExits(){
        when(userRepository.getByUsername("notAEmployee")).thenReturn(Optional.empty());
    }
}
