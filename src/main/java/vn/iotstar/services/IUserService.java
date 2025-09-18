package vn.iotstar.services;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import vn.iotstar.entities.UserEntity;
import vn.iotstar.repositories.IUserRepository;

public interface IUserService {

	void deleteAll();

	void deleteAll(Iterable<? extends UserEntity> entities);

	<S extends UserEntity> List<S> findAll(Example<S> example, Sort sort);

	<S extends UserEntity> List<S> findAll(Example<S> example);

	void deleteAllById(Iterable<? extends Integer> ids);

	UserEntity getReferenceById(Integer id);

	void delete(UserEntity entity);

	void deleteById(Integer id);

	UserEntity getById(Integer id);

	long count();

	<S extends UserEntity, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

	UserEntity getOne(Integer id);

	void deleteAllInBatch();

	<S extends UserEntity> boolean exists(Example<S> example);

	boolean existsById(Integer id);

	void deleteAllByIdInBatch(Iterable<Integer> ids);

	<S extends UserEntity> long count(Example<S> example);

	Optional<UserEntity> findById(Integer id);

	void deleteAllInBatch(Iterable<UserEntity> entities);

	<S extends UserEntity> Page<S> findAll(Example<S> example, Pageable pageable);

	void deleteInBatch(Iterable<UserEntity> entities);

	List<UserEntity> findAllById(Iterable<Integer> ids);

	List<UserEntity> findAll();

	<S extends UserEntity> List<S> saveAllAndFlush(Iterable<S> entities);

	<S extends UserEntity> S saveAndFlush(S entity);

	Page<UserEntity> findAll(Pageable pageable);

	void flush();

	<S extends UserEntity> S save(S entity);

	List<UserEntity> findAll(Sort sort);

	UserEntity findByUsername(String username);

	<S extends UserEntity> Optional<S> findOne(Example<S> example);

	<S extends UserEntity> List<S> saveAll(Iterable<S> entities);

	boolean checkExistPhone(String phone);

	boolean checkExistUsername(String username);

	boolean checkExistEmail(String email);

	UserEntity findByEmail(String email);

	UserEntity login(String username, String password);
	
	boolean register(String email, String password, String repeatPassword, String username, String fullname, String phone);
	
	boolean isPasswordMatch(String password, String repeatPassword);

	
}
