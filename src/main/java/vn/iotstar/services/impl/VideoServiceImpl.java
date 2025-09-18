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

import vn.iotstar.entities.VideoEntity;
import vn.iotstar.repositories.IVideoRespository;
import vn.iotstar.services.IVideoService;

@Service
public class VideoServiceImpl implements IVideoService{
	@Autowired
	IVideoRespository videoRepository;

	@Override
	public List<VideoEntity> findByTitleContaining(String name) {
		return videoRepository.findByTitleContaining(name);
	}

	@Override
	public Page<VideoEntity> findByTitleContaining(String name, Pageable pageable) {
		return videoRepository.findByTitleContaining(name, pageable);
	}

	@Override
	public <S extends VideoEntity> List<S> saveAll(Iterable<S> entities) {
		return videoRepository.saveAll(entities);
	}

	@Override
	public <S extends VideoEntity> Optional<S> findOne(Example<S> example) {
		return videoRepository.findOne(example);
	}

	@Override
	public List<VideoEntity> findAll(Sort sort) {
		return videoRepository.findAll(sort);
	}

	@Override
	public <S extends VideoEntity> S save(S entity) {
		return videoRepository.save(entity);
	}

	@Override
	public void flush() {
		videoRepository.flush();
	}

	@Override
	public Page<VideoEntity> findAll(Pageable pageable) {
		return videoRepository.findAll(pageable);
	}

	@Override
	public <S extends VideoEntity> S saveAndFlush(S entity) {
		return videoRepository.saveAndFlush(entity);
	}

	@Override
	public <S extends VideoEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
		return videoRepository.saveAllAndFlush(entities);
	}

	@Override
	public List<VideoEntity> findAll() {
		return videoRepository.findAll();
	}

	@Override
	public List<VideoEntity> findAllById(Iterable<Integer> ids) {
		return videoRepository.findAllById(ids);
	}

	@Override
	public void deleteInBatch(Iterable<VideoEntity> entities) {
		videoRepository.deleteInBatch(entities);
	}

	@Override
	public <S extends VideoEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
		return videoRepository.findAll(example, pageable);
	}

	@Override
	public void deleteAllInBatch(Iterable<VideoEntity> entities) {
		videoRepository.deleteAllInBatch(entities);
	}

	@Override
	public Optional<VideoEntity> findById(Integer id) {
		return videoRepository.findById(id);
	}

	@Override
	public <S extends VideoEntity> long count(Example<S> example) {
		return videoRepository.count(example);
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Integer> ids) {
		videoRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	public boolean existsById(Integer id) {
		return videoRepository.existsById(id);
	}

	@Override
	public <S extends VideoEntity> boolean exists(Example<S> example) {
		return videoRepository.exists(example);
	}

	@Override
	public void deleteAllInBatch() {
		videoRepository.deleteAllInBatch();
	}

	@Override
	public VideoEntity getOne(Integer id) {
		return videoRepository.getOne(id);
	}

	@Override
	public <S extends VideoEntity, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return videoRepository.findBy(example, queryFunction);
	}

	@Override
	public long count() {
		return videoRepository.count();
	}

	@Override
	public VideoEntity getById(Integer id) {
		return videoRepository.getById(id);
	}

	@Override
	public void deleteById(Integer id) {
		videoRepository.deleteById(id);
	}

	@Override
	public void delete(VideoEntity entity) {
		videoRepository.delete(entity);
	}

	@Override
	public VideoEntity getReferenceById(Integer id) {
		return videoRepository.getReferenceById(id);
	}

	@Override
	public void deleteAllById(Iterable<? extends Integer> ids) {
		videoRepository.deleteAllById(ids);
	}

	@Override
	public <S extends VideoEntity> List<S> findAll(Example<S> example) {
		return videoRepository.findAll(example);
	}

	@Override
	public <S extends VideoEntity> List<S> findAll(Example<S> example, Sort sort) {
		return videoRepository.findAll(example, sort);
	}

	@Override
	public void deleteAll(Iterable<? extends VideoEntity> entities) {
		videoRepository.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		videoRepository.deleteAll();
	}
	
	
}
