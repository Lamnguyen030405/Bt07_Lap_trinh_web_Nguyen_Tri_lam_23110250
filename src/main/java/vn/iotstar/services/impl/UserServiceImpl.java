package vn.iotstar.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import vn.iotstar.entities.UserEntity;
import vn.iotstar.repositories.IUserRepository;
import vn.iotstar.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	IUserRepository userRepository;

	@Override
	public UserEntity findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public boolean checkExistEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public boolean checkExistUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean checkExistPhone(String phone) {
		return userRepository.existsByPhone(phone);
	}

	@Override
	public <S extends UserEntity> List<S> saveAll(Iterable<S> entities) {
		return userRepository.saveAll(entities);
	}

	@Override
	public <S extends UserEntity> Optional<S> findOne(Example<S> example) {
		return userRepository.findOne(example);
	}

	@Override
	public UserEntity findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<UserEntity> findAll(Sort sort) {
		return userRepository.findAll(sort);
	}

	@Override
	public <S extends UserEntity> S save(S entity) {
		return userRepository.save(entity);
	}

	@Override
	public void flush() {
		userRepository.flush();
	}

	@Override
	public Page<UserEntity> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public <S extends UserEntity> S saveAndFlush(S entity) {
		return userRepository.saveAndFlush(entity);
	}

	@Override
	public <S extends UserEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
		return userRepository.saveAllAndFlush(entities);
	}

	@Override
	public List<UserEntity> findAll() {
		return userRepository.findAll();
	}

	@Override
	public List<UserEntity> findAllById(Iterable<Integer> ids) {
		return userRepository.findAllById(ids);
	}

	@Override
	public void deleteInBatch(Iterable<UserEntity> entities) {
		userRepository.deleteInBatch(entities);
	}

	@Override
	public <S extends UserEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
		return userRepository.findAll(example, pageable);
	}

	@Override
	public void deleteAllInBatch(Iterable<UserEntity> entities) {
		userRepository.deleteAllInBatch(entities);
	}

	@Override
	public Optional<UserEntity> findById(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	public <S extends UserEntity> long count(Example<S> example) {
		return userRepository.count(example);
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Integer> ids) {
		userRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	public boolean existsById(Integer id) {
		return userRepository.existsById(id);
	}

	@Override
	public <S extends UserEntity> boolean exists(Example<S> example) {
		return userRepository.exists(example);
	}

	@Override
	public void deleteAllInBatch() {
		userRepository.deleteAllInBatch();
	}

	@Override
	public UserEntity getOne(Integer id) {
		return userRepository.getOne(id);
	}

	@Override
	public <S extends UserEntity, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return userRepository.findBy(example, queryFunction);
	}

	@Override
	public long count() {
		return userRepository.count();
	}

	@Override
	public UserEntity getById(Integer id) {
		return userRepository.getById(id);
	}

	@Override
	public void deleteById(Integer id) {
		userRepository.deleteById(id);
	}

	@Override
	public void delete(UserEntity entity) {
		userRepository.delete(entity);
	}

	@Override
	public UserEntity getReferenceById(Integer id) {
		return userRepository.getReferenceById(id);
	}

	@Override
	public void deleteAllById(Iterable<? extends Integer> ids) {
		userRepository.deleteAllById(ids);
	}

	@Override
	public <S extends UserEntity> List<S> findAll(Example<S> example) {
		return userRepository.findAll(example);
	}

	@Override
	public <S extends UserEntity> List<S> findAll(Example<S> example, Sort sort) {
		return userRepository.findAll(example, sort);
	}

	@Override
	public void deleteAll(Iterable<? extends UserEntity> entities) {
		userRepository.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	}

	@Override
	public UserEntity login(String username, String password) {
		UserEntity user = this.findByUsername(username);

		if (user != null && password.equals(user.getPassword())) {
			user.setStatus(1);
			return user;
		}
		return null;
	}

	@Override
	public boolean register(String email, String password, String repeatPassword, String username, String fullname,
			String phone) {
		if (!this.isPasswordMatch(password, repeatPassword)) {
			return false;
		}
		if (this.checkExistUsername(username)) {
			return false;
		}
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		UserEntity user = new UserEntity();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setFullname(fullname);
		user.setPhone(phone);
		user.setCreatedate(date);
		user.setRoleid(5);
		userRepository.save(user);
		return true;
	}

	@Override
	public boolean isPasswordMatch(String password, String repeatPassword) {
		return password != null && password.equals(repeatPassword);
	}

}
