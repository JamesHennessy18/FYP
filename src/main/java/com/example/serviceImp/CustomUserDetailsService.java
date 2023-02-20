package com.example.serviceImp;

import com.example.Model.User;
import com.example.Repo.UserRepository;
import com.example.Service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository repo;

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

		Optional<User> optionalUser = repo.findById(userInForm.getId());
		if(!optionalUser.isPresent()){
			System.out.println("User not found.");
			return null;
		}

		User userInDB = optionalUser.get();
		System.out.println("User fetched: " + userInDB);


		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		System.out.println("Saving updated user: " + userInDB);

		User savedUser = repo.save(userInDB);
		System.out.println("User saved: " + savedUser);

		return savedUser;
	}

}
