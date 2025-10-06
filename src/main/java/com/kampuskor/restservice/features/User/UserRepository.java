package com.kampuskor.restservice.features.User;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.kampuskor.restservice.features.User.enums.RoleType;

public interface UserRepository extends CrudRepository<User, Long>,
PagingAndSortingRepository<User, Long> {
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);
  Optional<User> findByUsernameOrEmail(String username, String email);
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);

  Page<User> findByRoleType(RoleType roleType, PageRequest pageRequest);
}
