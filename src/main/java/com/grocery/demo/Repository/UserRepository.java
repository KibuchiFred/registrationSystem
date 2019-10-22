package com.grocery.demo.Repository;

import com.grocery.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
User findByUname(String uname);
User findByEmailIgnoreCase(String email);
}
