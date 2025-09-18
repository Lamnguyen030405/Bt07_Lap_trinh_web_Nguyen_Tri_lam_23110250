package vn.iotstar.controllers.user;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.entities.CategoryEntity;
import vn.iotstar.entities.UserEntity;
import vn.iotstar.services.ICategoryService;
import vn.iotstar.utils.UploadUtils;
import vn.iotstar.utils.path.Constant;

@Controller("userCategoryController")
@RequestMapping("user/categories")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;

    @GetMapping("add")
    public String add(ModelMap model) {
        return "user/categories/add";
    }

    @PostMapping("add")
    public String addPost(@RequestParam("name") String name,
                          @RequestPart("icon") Part file,
                          HttpSession session,
                          ModelMap model) {

        try {
            CategoryEntity category = new CategoryEntity();
            category.setCatename(name);

            UserEntity user = (UserEntity) session.getAttribute("account");
            if (user == null) {
                model.addAttribute("error", "Vui lòng đăng nhập trước khi thêm danh mục.");
                return "user/categories/add";
            }

            category.setUserid(user.getId());

            String iconPath = UploadUtils.processUploadFile(file, Constant.DIR + "/category");
            if (iconPath != null) {
                category.setIcon(iconPath);
            }

            categoryService.save(category);
            return "redirect:/user/categories";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi thêm danh mục.");
            return "user/categories/add";
        }
    }

    @RequestMapping("")
    public String list(ModelMap model,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("cateid").descending());
        Page<CategoryEntity> categories = categoryService.findAll(pageable);

        model.addAttribute("cateList", categories.getContent()); // dùng trong <c:forEach>
        model.addAttribute("categories", categories); // dùng cho phân trang
        return "user/categories/list";
    }

    @GetMapping("delete")
    public String delete(@RequestParam("id") int categoryId, HttpSession session) {
        try {
            categoryService.deleteById(categoryId);
            session.setAttribute("message", "Xóa danh mục thành công.");
        } catch (Exception e) {
            session.setAttribute("message", "Lỗi khi xóa danh mục.");
        }
        return "redirect:/user/categories";
    }

    @RequestMapping("search")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
        if (name != null && !name.isBlank()) {
            model.addAttribute("cateList", categoryService.findByCatenameContaining(name));
        } else {
            model.addAttribute("cateList", categoryService.findAll());
        }
        return "user/categories/search";
    }

    @GetMapping("/image")
    public void download(@RequestParam("fname") String fileName, HttpServletResponse resp) throws IOException {
        File file = new File(Constant.DIR + File.separator + fileName);
        if (file.exists()) {
            Files.copy(file.toPath(), resp.getOutputStream());
            resp.flushBuffer();
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @GetMapping("edit")
    public String editForm(@RequestParam("id") int id, ModelMap model) {
        CategoryEntity category = categoryService.findById(id).orElse(null);
        if (category == null) {
            model.addAttribute("error", "Không tìm thấy danh mục.");
            return "redirect:/user/categories";
        }
        model.addAttribute("category", category);
        return "user/categories/edit";
    }
    
    @PostMapping("edit")
    public String editPost(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestPart("icon") Part file,
            HttpSession session,
            ModelMap model) {

        try {
            CategoryEntity category = categoryService.findById(id).orElse(null);
            if (category == null) {
                model.addAttribute("error", "Không tìm thấy danh mục để cập nhật.");
                return "user/categories/edit";
            }

            category.setCatename(name);

            // Lấy user từ session
            UserEntity user = (UserEntity) session.getAttribute("account");
            if (user != null) {
                category.setUserid(user.getId());
            }

            // Nếu người dùng chọn ảnh mới
            if (file != null && file.getSize() > 0) {
                String iconPath = UploadUtils.processUploadFile(file, Constant.DIR + "/category");
                if (iconPath != null) {
                    category.setIcon(iconPath);
                }
            }

            categoryService.save(category); // Gọi lại save để update
            return "redirect:/user/categories";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật danh mục.");
            return "user/categories/edit";
        }
    }


}
