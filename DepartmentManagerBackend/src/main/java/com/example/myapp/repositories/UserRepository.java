package com.example.myapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.entities.Directorate;
import com.example.myapp.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT * FROM users WHERE department = :dep ORDER BY"
			+ " position DESC, user_id DESC LIMIT 10 OFFSET :offset", nativeQuery = true)
    List<User> find3(
    		@Param("offset") int offset,
    		@Param("dep") Long id
    		);
	
	@Query(value = "SELECT * FROM users WHERE department = :dep AND name LIKE CONCAT(:keyword,'%')"
			+ " OR department = :dep AND surname LIKE CONCAT(:keyword,'%')"
			+ " ORDER BY user_id DESC LIMIT 10 OFFSET :offset", nativeQuery = true)
	List<User> searchUsers(
			@Param("keyword") String keyword,
			@Param("offset") int offset,
			@Param("dep") Long id
			);
	
	@Query(value = "SELECT COUNT(DISTINCT user_id) FROM users WHERE department = :dep", nativeQuery = true)
	int getUserCount(
			@Param("dep") Long id
			);
	
	@Query(value = "SELECT COUNT(DISTINCT user_id) FROM users WHERE department = :dep"
			+ " AND name LIKE CONCAT(:keyword,'%') OR department = :dep AND"
			+ " surname LIKE CONCAT(:keyword,'%')", nativeQuery = true)
	int getFilteredUserCount(
			@Param("keyword") String keyword,
			@Param("dep") Long id
			);
	
	@Query(value = "SELECT * FROM users WHERE ban_expiration_date IS NOT NULL", nativeQuery = true)
	List<User> findBannedUsers();
    
    User findByUsername(String username);
    
    User findByName(String name);
    
    User findByUCN(String UCN);

    boolean existsByUCN(String UCN);
    
    boolean existsByUsername(String username);
}
