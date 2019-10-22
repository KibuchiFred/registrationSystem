package com.grocery.demo.Service;

import com.grocery.demo.Model.User;
import com.grocery.demo.Repository.RoleRepository;
import com.grocery.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public void save(User user) {
        user.setEnabled(false);
        user.setPword(new BCryptPasswordEncoder().encode(user.getPword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);

    }

    public User findByEmail(String email){
        return userRepository.findByEmailIgnoreCase(email);
    }

    public User findOne(Long id){
        //findById is a CrudRepository method. it replaced findOne CrudRepostory method in earlier spring boot versions
        return userRepository.getOne(id);//define this method in the repository
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
}