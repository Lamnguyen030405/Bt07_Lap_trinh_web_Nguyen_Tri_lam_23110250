package vn.iotstar.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import vn.iotstar.entities.ProductEntity;

public interface IProductService {

	void deleteAll();

	void deleteAll(Iterable<? extends ProductEntity> entities);

	<S extends ProductEntity> List<S> findAll(Example<S> example, Sort sort);

	<S extends ProductEntity> List<S> findAll(Example<S> example);

	void deleteAllById(Iterable<? extends Long> ids);

	ProductEntity getReferenceById(Long id);

	void delete(ProductEntity entity);

	void deleteById(Long id);

	ProductEntity getById(Long id);

	long count();

	<S extends ProductEntity, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

	ProductEntity getOne(Long id);

	void deleteAllInBatch();

	<S extends ProductEntity> boolean exists(Example<S> example);

	boolean existsById(Long id);

	void deleteAllByIdInBatch(Iterable<Long> ids);

	<S extends ProductEntity> long count(Example<S> example);

	Optional<ProductEntity> findById(Long id);

	void deleteAllInBatch(Iterable<ProductEntity> entities);

	<S extends ProductEntity> Page<S> findAll(Example<S> example, Pageable pageable);

	void deleteInBatch(Iterable<ProductEntity> entities);

	List<ProductEntity> findAllById(Iterable<Long> ids);

	List<ProductEntity> findAll();

	<S extends ProductEntity> List<S> saveAllAndFlush(Iterable<S> entities);

	<S extends ProductEntity> S saveAndFlush(S entity);

	Page<ProductEntity> findAll(Pageable pageable);

	void flush();

	<S extends ProductEntity> S save(S entity);

	List<ProductEntity> findAll(Sort sort);

	<S extends ProductEntity> Optional<S> findOne(Example<S> example);

	<S extends ProductEntity> List<S> saveAll(Iterable<S> entities);

	Optional<ProductEntity> findByCreatedate(Date createAt);

	Optional<ProductEntity> findByProductname(String name);

	Page<ProductEntity> findByProductnameContaining(String name, Pageable pageable);

	List<ProductEntity> findByProductnameContaining(String name);

}
