package vn.iotstar.services;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import vn.iotstar.entities.CategoryEntity;

public interface ICategoryService {

	void deleteAll();

	void deleteAll(Iterable<? extends CategoryEntity> entities);

	<S extends CategoryEntity> List<S> findAll(Example<S> example, Sort sort);

	<S extends CategoryEntity> List<S> findAll(Example<S> example);

	void deleteAllById(Iterable<? extends Integer> ids);

	CategoryEntity getReferenceById(Integer id);

	void delete(CategoryEntity entity);

	void deleteById(Integer id);

	CategoryEntity getById(Integer id);

	long count();

	<S extends CategoryEntity, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

	CategoryEntity getOne(Integer id);

	void deleteAllInBatch();

	<S extends CategoryEntity> boolean exists(Example<S> example);

	boolean existsById(Integer id);

	void deleteAllByIdInBatch(Iterable<Integer> ids);

	<S extends CategoryEntity> long count(Example<S> example);

	Optional<CategoryEntity> findById(Integer id);

	void deleteAllInBatch(Iterable<CategoryEntity> entities);

	<S extends CategoryEntity> Page<S> findAll(Example<S> example, Pageable pageable);

	void deleteInBatch(Iterable<CategoryEntity> entities);

	List<CategoryEntity> findAllById(Iterable<Integer> ids);

	List<CategoryEntity> findAll();

	<S extends CategoryEntity> List<S> saveAllAndFlush(Iterable<S> entities);

	<S extends CategoryEntity> S saveAndFlush(S entity);

	Page<CategoryEntity> findAll(Pageable pageable);

	void flush();

	<S extends CategoryEntity> S save(S entity);

	List<CategoryEntity> findAll(Sort sort);

	<S extends CategoryEntity> Optional<S> findOne(Example<S> example);

	<S extends CategoryEntity> List<S> saveAll(Iterable<S> entities);

	Page<CategoryEntity> findByCatenameContaining(String name, Pageable pageable);

	List<CategoryEntity> findByCatenameContaining(String name);

	List<CategoryEntity> findByUserid(int userid);

	Optional<CategoryEntity> findByCatename(String name);
	
	

}
