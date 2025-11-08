package com.cybernauts.backend;

import com.cybernauts.backend.exception.RelationshipConflictException;
import com.cybernauts.backend.exception.UserNotFoundException;
import com.cybernauts.backend.model.User;
import com.cybernauts.backend.repository.UserRepository;
import com.cybernauts.backend.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BackendApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	private User user1;
	private User user2;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();

		user1 = new User();
		user1.setUsername("Alice");
		user1.setAge(25);
		user1.setHobbies(new HashSet<>());
		user1 = userService.createUser(user1);

		user2 = new User();
		user2.setUsername("Bob");
		user2.setAge(30);
		user2.setHobbies(new HashSet<>());
		user2 = userService.createUser(user2);
	}

	// -------------------- Friendship Tests --------------------

	@Test
	void testLinkUsers() {
		userService.linkUsers(user1.getId(), user2.getId());

		User updatedUser1 = userRepository.findById(user1.getId()).get();
		User updatedUser2 = userRepository.findById(user2.getId()).get();

		assertTrue(updatedUser1.getFriends().contains(updatedUser2));
		assertTrue(updatedUser2.getFriends().contains(updatedUser1));
	}

	@Test
	void testUnlinkUsers() {
		userService.linkUsers(user1.getId(), user2.getId());
		userService.unlinkUsers(user1.getId(), user2.getId());

		User updatedUser1 = userRepository.findById(user1.getId()).get();
		User updatedUser2 = userRepository.findById(user2.getId()).get();

		assertFalse(updatedUser1.getFriends().contains(updatedUser2));
		assertFalse(updatedUser2.getFriends().contains(updatedUser1));
	}

	// -------------------- Deletion Rules --------------------

	@Test
	void testDeleteUserWithFriendThrowsException() {
		userService.linkUsers(user1.getId(), user2.getId());

		assertThrows(RelationshipConflictException.class, () -> {
			userService.deleteUser(user1.getId());
		});
	}

	@Test
	void testDeleteUserWithoutFriend() {
		userService.deleteUser(user1.getId());
		assertFalse(userRepository.findById(user1.getId()).isPresent());
	}

	// -------------------- Popularity Score --------------------

	@Test
	void testPopularityScore() {
		user1.getHobbies().add("Reading");
		user2.getHobbies().add("Reading");
		user2.getHobbies().add("Gaming");
		userRepository.save(user1);
		userRepository.save(user2);

		userService.linkUsers(user1.getId(), user2.getId());

		double scoreUser1 = userService.computePopularityScore(user1);
		double scoreUser2 = userService.computePopularityScore(user2);

		// User1: 1 friend + 0.5 * 1 shared hobby = 1.5
		assertEquals(1.5, scoreUser1);
		// User2: 1 friend + 0.5 * 1 shared hobby = 1.5
		assertEquals(1.5, scoreUser2);
	}

	// -------------------- Not Found Exceptions --------------------

	@Test
	void testUserNotFound() {
		UUID randomId = UUID.randomUUID();
		assertThrows(UserNotFoundException.class, () -> userService.deleteUser(randomId));
	}
}
