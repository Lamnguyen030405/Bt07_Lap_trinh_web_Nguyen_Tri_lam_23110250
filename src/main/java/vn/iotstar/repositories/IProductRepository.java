package vn.iotstar.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import vn.iotstar.entities.ProductEntity;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, Long> {

	// Tìm Kiếm theo nội dung tên
	List<ProductEntity> findByProductnameContaining(String name);

	// Tìm kiếm và Phân trang
	Page<ProductEntity> findByProductnameContaining(String name, Pageable pageable);

	Optional<ProductEntity> findByProductname(String name);

	Optional<ProductEntity> findByCreatedate(Date createAt);

}
