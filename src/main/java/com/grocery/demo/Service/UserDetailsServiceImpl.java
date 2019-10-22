package com.grocery.demo.Service;


import com.grocery.demo.Model.Role;
import com.grocery.demo.Model.User;
import com.grocery.demo.Repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;//has a method to find a user by username

    public UserDetailsServiceImpl(UserRepository userRepository){

        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = this.userRepository.findByUname(s);

        //if a username that does not exist is input by the user
        if(user == null) {
            throw new UsernameNotFoundException("User not found" + s);
        }
        else {

           // UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user);
            // return userDetailsImpl;

            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//
//            for(Role role : user.getRoles()){
//                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
//            }

            return new org.springframework.security.core.userdetails.User(user.getUname(),
                    user.getPword(), grantedAuthorities);
        }


    }
}
