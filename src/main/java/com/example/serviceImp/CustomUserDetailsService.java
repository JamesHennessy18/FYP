package com.example.serviceImp;

import com.example.Model.ChatMessages;
import com.example.Model.User;
import com.example.Repo.ChatRepository;
import com.example.Repo.UserRepository;
import com.example.Service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository repo;

	@Autowired
	private ChatRepository chatRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = repo.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("User does not exist");
		
		}
		return new CustomUserDetails(user);
	}

	@Transactional
	public User updateAccount(User userInForm){
		System.out.println("Fetching user with id: " + userInForm.getId());

		User userInDB = repo.findByEmail(userInForm.getEmail());
		if(userInDB == null){
			System.out.println("User not found.");
			return null;
		}

		//User userInDB = userInDb.get();
		System.out.println("User fetched: " + userInDB);


		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		userInDB.setAddress1(userInForm.getAddress1());
		userInDB.setAddress2(userInForm.getAddress2());
		userInDB.setCountry(userInForm.getCountry());

		System.out.println("Saving updated user: " + userInDB);

		User savedUser = repo.save(userInDB);
		System.out.println("User saved: " + savedUser);

		return savedUser;
	}
	public List<ChatMessages> findMessagesBySenderId(Long id) {
		return chatRepository.findBySenderId(id);
	}

	@Transactional
	public void delete(Long id) {
		chatRepository.deleteBySenderId(id);
		repo.deleteById(id);
	}
}
