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

import vn.iotstar.entities.CategoryEntity;
import vn.iotstar.repositories.ICategoryRepository;
import vn.iotstar.services.ICategoryService;

@Service

public class CategoryServiceImpl implements ICategoryService {

	@Autowired

	ICategoryRepository categoryRepository;

	public List<CategoryEntity> findByCatenameContaining(String name) {
		return categoryRepository.findByCatenameContaining(name);
	}

	public Page<CategoryEntity> findByCatenameContaining(String name, Pageable pageable) {
		return categoryRepository.findByCatenameContaining(name, pageable);
	}

	public <S extends CategoryEntity> List<S> saveAll(Iterable<S> entities) {
		return categoryRepository.saveAll(entities);
	}

	public <S extends CategoryEntity> Optional<S> findOne(Example<S> example) {
		return categoryRepository.findOne(example);
	}

	public List<CategoryEntity> findAll(Sort sort) {
		return categoryRepository.findAll(sort);
	}

	public <S extends CategoryEntity> S save(S entity) {
		return categoryRepository.save(entity);
	}

	public void flush() {
		categoryRepository.flush();
	}

	public Page<CategoryEntity> findAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

	public <S extends CategoryEntity> S saveAndFlush(S entity) {
		return categoryRepository.saveAndFlush(entity);
	}

	public <S extends CategoryEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
		return categoryRepository.saveAllAndFlush(entities);
	}

	public List<CategoryEntity> findAll() {
		return categoryRepository.findAll();
	}

	public List<CategoryEntity> findAllById(Iterable<Integer> ids) {
		return categoryRepository.findAllById(ids);
	}

	public void deleteInBatch(Iterable<CategoryEntity> entities) {
		categoryRepository.deleteInBatch(entities);
	}

	public <S extends CategoryEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
		return categoryRepository.findAll(example, pageable);
	}

	public void deleteAllInBatch(Iterable<CategoryEntity> entities) {
		categoryRepository.deleteAllInBatch(entities);
	}

	public Optional<CategoryEntity> findById(Integer id) {
		return categoryRepository.findById(id);
	}

	public <S extends CategoryEntity> long count(Example<S> example) {
		return categoryRepository.count(example);
	}

	public void deleteAllByIdInBatch(Iterable<Integer> ids) {
		categoryRepository.deleteAllByIdInBatch(ids);
	}

	public boolean existsById(Integer id) {
		return categoryRepository.existsById(id);
	}

	public <S extends CategoryEntity> boolean exists(Example<S> example) {
		return categoryRepository.exists(example);
	}

	public void deleteAllInBatch() {
		categoryRepository.deleteAllInBatch();
	}

	public CategoryEntity getOne(Integer id) {
		return categoryRepository.getOne(id);
	}

	public <S extends CategoryEntity, R> R findBy(Example<S> example,
			Function<FetchableFluentQuery<S>, R> queryFunction) {
		return categoryRepository.findBy(example, queryFunction);
	}

	public long count() {
		return categoryRepository.count();
	}

	public CategoryEntity getById(Integer id) {
		return categoryRepository.getById(id);
	}

	@Override
	public List<CategoryEntity> findByUserid(int userid) {
		return categoryRepository.findByUserid(userid);
	}

	public void deleteById(Integer id) {
		categoryRepository.deleteById(id);
	}

	public void delete(CategoryEntity entity) {
		categoryRepository.delete(entity);
	}

	public CategoryEntity getReferenceById(Integer id) {
		return categoryRepository.getReferenceById(id);
	}

	public void deleteAllById(Iterable<? extends Integer> ids) {
		categoryRepository.deleteAllById(ids);
	}

	public <S extends CategoryEntity> List<S> findAll(Example<S> example) {
		return categoryRepository.findAll(example);
	}

	public <S extends CategoryEntity> List<S> findAll(Example<S> example, Sort sort) {
		return categoryRepository.findAll(example, sort);
	}

	public void deleteAll(Iterable<? extends CategoryEntity> entities) {
		categoryRepository.deleteAll(entities);
	}

	public void deleteAll() {
		categoryRepository.deleteAll();
	}
	
	
	
	
}
