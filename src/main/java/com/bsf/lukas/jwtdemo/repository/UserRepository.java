package com.bsf.lukas.jwtdemo.repository;

import com.bsf.lukas.jwtdemo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findFirstByUserName(String name);

}
