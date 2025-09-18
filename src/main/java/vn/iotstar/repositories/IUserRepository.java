package vn.iotstar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entities.UserEntity;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Integer>{
	
	// Tìm theo email
    UserEntity findByEmail(String email);

    // Tìm theo username
    UserEntity findByUsername(String username);

    // Kiểm tra tồn tại
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);
}
