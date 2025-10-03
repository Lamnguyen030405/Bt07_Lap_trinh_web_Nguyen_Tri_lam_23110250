package vn.iotstar.controllers.api;

import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import vn.iotstar.models.ResponseModel;
import vn.iotstar.services.ICategoryService;
import vn.iotstar.services.IStorageService;

@RestController
@RequestMapping(path = "/api/category")
public class CategoryAPIController {
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	IStorageService storageService;

//	@GetMapping
//	public ResponseEntity<?> getAllCategory() {
////		 return ResponseEntity.ok().body(categoryService.findAll());
//		return new ResponseEntity<ResponseModel>(new ResponseModel(true, "Thành công", categoryService.findAll()),
//				HttpStatus.OK);
//	}
	
	@GetMapping
	public ResponseEntity<?> getAllCategoriesPaged(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size) {

	    Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by("cateid").descending());
	    Page<CategoryEntity> categories = categoryService.findAll(pageable);

	    return ResponseEntity.ok().body(Map.of(
	        "status", true,
	        "message", "Thành công",
	        "data", categories.getContent(),
	        "totalPages", categories.getTotalPages(),
	        "currentPage", categories.getNumber()
	    ));
	}
	
	@GetMapping("/t")
	public ResponseEntity<?> getT() {
		 return ResponseEntity.ok().body(categoryService.findAll());
		//return new ResponseEntity<ResponseModel>(new ResponseModel(true, "Thành công", categoryService.findAll()),
				//HttpStatus.OK);
	}

	@PostMapping(path = "/getCategory")
	public ResponseEntity<?> getCategory(@Validated @RequestParam("id") int id) {
		Optional<CategoryEntity> category = categoryService.findById(id);
		if (category.isPresent()) {
			// return ResponseEntity.ok().body(category.get());
			return new ResponseEntity<ResponseModel>(new ResponseModel(true, "Thành công", category.get()),
					HttpStatus.OK);
		} else {
			// return ResponseEntity.notFound().build();
			return new ResponseEntity<ResponseModel>(new ResponseModel(false, "Thất bại", null), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(path = "/addCategory")
	public ResponseEntity<?> addCategory(@Validated @RequestParam("categoryName") String categoryName,
			@Validated @RequestParam("icon") MultipartFile icon) {
		Optional<CategoryEntity> optCategory = categoryService.findByCatename(categoryName);
		if (optCategory.isPresent()) {
			// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category đã tồn
			// tại trong hệ thống");
			return new ResponseEntity<ResponseModel>(
					new ResponseModel(false, "Loại sản phẩm này đã tồn tại trong hệ thống", optCategory.get()),
					HttpStatus.BAD_REQUEST);
		} else {
			CategoryEntity category = new CategoryEntity();
//kiểm tra tồn tại file, lưu file
			if (!icon.isEmpty()) {
				UUID uuid = UUID.randomUUID();
				String uuString = uuid.toString();
//lưu file vào trường Images
				category.setIcon(storageService.getSorageFilename(icon, uuString));
				storageService.store(icon, category.getIcon());
			}
			category.setCatename(categoryName);
			categoryService.save(category);
//return ResponseEntity.ok().body(category);
			return new ResponseEntity<ResponseModel>(new ResponseModel(true, "Thêm thành công", category),
					HttpStatus.OK);
		}
	}

	@PutMapping(path = "/updateCategory")
	public ResponseEntity<?> updateCategory(@Validated @RequestParam("categoryId") int categoryId,
			@Validated @RequestParam("categoryName") String categoryName,
			@Validated @RequestParam("icon") MultipartFile icon) {
		Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);
		if (optCategory.isEmpty()) {
			return new ResponseEntity<ResponseModel>(new ResponseModel(false, "Không tìm thấy Category", null),
					HttpStatus.BAD_REQUEST);
		} else if (optCategory.isPresent()) {
//kiểm tra tồn tại file, lưu file
			if (!icon.isEmpty()) {
				UUID uuid = UUID.randomUUID();
				String uuString = uuid.toString();
//lưu file vào trường Images
				optCategory.get().setIcon(storageService.getSorageFilename(icon, uuString));
				storageService.store(icon, optCategory.get().getIcon());
			}
			optCategory.get().setCatename(categoryName);
			categoryService.save(optCategory.get());
//return ResponseEntity.ok().body(category);
			return new ResponseEntity<ResponseModel>(new ResponseModel(true, "Cập nhật Thành công", optCategory.get()),
					HttpStatus.OK);
		}
		return null;
	}

	@DeleteMapping(path = "/deleteCategory")
	public ResponseEntity<?> deleteCategory(@Validated @RequestParam("categoryId") int categoryId) {
		Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);
		if (optCategory.isEmpty()) {
			return new ResponseEntity<ResponseModel>(new ResponseModel(false, "Không tìm thấy Category", null),
					HttpStatus.BAD_REQUEST);
		} else if (optCategory.isPresent()) {
			categoryService.delete(optCategory.get());
//return ResponseEntity.ok().body(category);
			return new ResponseEntity<ResponseModel>(new ResponseModel(true, "Xóa thành công", optCategory.get()),
					HttpStatus.OK);
		}
		return null;
	}
}