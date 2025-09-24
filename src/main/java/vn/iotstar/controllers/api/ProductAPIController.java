package vn.iotstar.controllers.api;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.entities.CategoryEntity;
import vn.iotstar.entities.ProductEntity;
import vn.iotstar.models.ResponseModel;
import vn.iotstar.services.ICategoryService;
import vn.iotstar.services.IProductService;
import vn.iotstar.services.IStorageService;

@RestController
@RequestMapping(path = "/api/product")
public class ProductAPIController {
	@Autowired
	private IProductService productService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	IStorageService storageService;

	@GetMapping
	public ResponseEntity<?> getAllProduct() {
		return new ResponseEntity<ResponseModel>(new ResponseModel(true, "Thành công", productService.findAll()),
				HttpStatus.OK);
	}

	@PostMapping(path = "/getProduct")
	public ResponseEntity<?> getProduct(@Validated @RequestParam("id") Long id) {
		Optional<ProductEntity> product = productService.findById(id);
		if (product.isPresent()) {
			return new ResponseEntity<ResponseModel>(new ResponseModel(true, "Thành công", product.get()),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<ResponseModel>(new ResponseModel(false, "Thất bại", null), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(path = "/addProduct")
	public ResponseEntity<?> addProduct(@Validated @RequestParam("productName") String productName,
			@Validated @RequestParam("quantity") int quantity, @Validated @RequestParam("unitPrice") double unitPrice,
			@Validated @RequestParam("description") String description,
			@Validated @RequestParam("discount") double discount, @Validated @RequestParam("status") short status,
			@Validated @RequestParam("categoryId") int categoryId, @RequestParam("icon") MultipartFile icon) {

		// Kiểm tra sản phẩm đã tồn tại
		Optional<ProductEntity> optProduct = productService.findByProductname(productName);
		if (optProduct.isPresent()) {
			return new ResponseEntity<ResponseModel>(
					new ResponseModel(false, "Sản phẩm này đã tồn tại trong hệ thống", optProduct.get()),
					HttpStatus.BAD_REQUEST);
		}

		ProductEntity product = new ProductEntity();
		product.setProductname(productName);
		product.setQuantity(quantity);
		product.setUnitprice(unitPrice);
		product.setDescription(description);
		product.setDiscount(discount);
		product.setStatus(status);
		product.setCreatedate(new Date());

		// Gán category
		Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);
		if (optCategory.isPresent()) {
			product.setCategory(optCategory.get());
		} else {
			return new ResponseEntity<ResponseModel>(new ResponseModel(false, "Category không tồn tại", null),
					HttpStatus.BAD_REQUEST);
		}

		// Lưu file nếu có
		if (!icon.isEmpty()) {
			UUID uuid = UUID.randomUUID();
			String filename = storageService.getSorageFilename(icon, uuid.toString());
			product.setImages(filename);
			storageService.store(icon, filename);
		}

		// Lưu product
		productService.save(product);

		return new ResponseEntity<ResponseModel>(new ResponseModel(true, "Thêm sản phẩm thành công", product),
				HttpStatus.OK);
	}

	@PutMapping(path = "/updateProduct")
	public ResponseEntity<?> updateProduct(@Validated @RequestParam("productId") Long productId,
			@RequestParam("productName") String productName, @RequestParam("quantity") int quantity,
			@RequestParam("unitPrice") double unitPrice, @RequestParam("description") String description,
			@RequestParam("discount") double discount, @RequestParam("status") short status,
			@RequestParam("categoryId") int categoryId,
			@RequestParam(value = "icon", required = false) MultipartFile icon) {

		Optional<ProductEntity> optProduct = productService.findById(productId);
		if (optProduct.isEmpty()) {
			return new ResponseEntity<ResponseModel>(new ResponseModel(false, "Không tìm thấy sản phẩm", null),
					HttpStatus.BAD_REQUEST);
		}

		ProductEntity product = optProduct.get();

		// Cập nhật các trường cơ bản
		product.setProductname(productName);
		product.setQuantity(quantity);
		product.setUnitprice(unitPrice);
		product.setDescription(description);
		product.setDiscount(discount);
		product.setStatus(status);

		// Cập nhật category
		Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);
		if (optCategory.isEmpty()) {
			return new ResponseEntity<ResponseModel>(new ResponseModel(false, "Category không tồn tại", null),
					HttpStatus.BAD_REQUEST);
		}
		product.setCategory(optCategory.get());

		// Lưu file nếu có
		if (icon != null && !icon.isEmpty()) {
			UUID uuid = UUID.randomUUID();
			String filename = storageService.getSorageFilename(icon, uuid.toString());
			product.setImages(filename);
			storageService.store(icon, filename);
		}

		// Lưu product
		productService.save(product);

		return new ResponseEntity<ResponseModel>(new ResponseModel(true, "Cập nhật sản phẩm thành công", product),
				HttpStatus.OK);
	}

	@DeleteMapping(path = "/deleteProduct")
	public ResponseEntity<?> deleteProduct(@Validated @RequestParam("productId") Long productId) {
		Optional<ProductEntity> optProduct = productService.findById(productId);
		if (optProduct.isEmpty()) {
			return new ResponseEntity<ResponseModel>(new ResponseModel(false, "Không tìm thấy sản phẩm", null),
					HttpStatus.BAD_REQUEST);
		}

		productService.delete(optProduct.get());

		return new ResponseEntity<ResponseModel>(new ResponseModel(true, "Xóa sản phẩm thành công", optProduct.get()),
				HttpStatus.OK);
	}

}