package vn.iotstar.services;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import vn.iotstar.entities.VideoEntity;

public interface IVideoService {

	void deleteAll();

	void deleteAll(Iterable<? extends VideoEntity> entities);

	<S extends VideoEntity> List<S> findAll(Example<S> example, Sort sort);

	<S extends VideoEntity> List<S> findAll(Example<S> example);

	void deleteAllById(Iterable<? extends Integer> ids);

	VideoEntity getReferenceById(Integer id);

	void delete(VideoEntity entity);

	void deleteById(Integer id);

	VideoEntity getById(Integer id);

	long count();

	<S extends VideoEntity, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

	VideoEntity getOne(Integer id);

	void deleteAllInBatch();

	<S extends VideoEntity> boolean exists(Example<S> example);

	boolean existsById(Integer id);

	void deleteAllByIdInBatch(Iterable<Integer> ids);

	<S extends VideoEntity> long count(Example<S> example);

	Optional<VideoEntity> findById(Integer id);

	void deleteAllInBatch(Iterable<VideoEntity> entities);

	<S extends VideoEntity> Page<S> findAll(Example<S> example, Pageable pageable);

	void deleteInBatch(Iterable<VideoEntity> entities);

	List<VideoEntity> findAllById(Iterable<Integer> ids);

	List<VideoEntity> findAll();

	<S extends VideoEntity> List<S> saveAllAndFlush(Iterable<S> entities);

	<S extends VideoEntity> S saveAndFlush(S entity);

	Page<VideoEntity> findAll(Pageable pageable);

	void flush();

	<S extends VideoEntity> S save(S entity);

	List<VideoEntity> findAll(Sort sort);

	<S extends VideoEntity> Optional<S> findOne(Example<S> example);

	<S extends VideoEntity> List<S> saveAll(Iterable<S> entities);

	Page<VideoEntity> findByTitleContaining(String name, Pageable pageable);

	List<VideoEntity> findByTitleContaining(String name);

	
}
