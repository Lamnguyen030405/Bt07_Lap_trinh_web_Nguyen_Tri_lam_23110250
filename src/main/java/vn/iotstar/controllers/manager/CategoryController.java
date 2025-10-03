package vn.iotstar.controllers.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import jakarta.validation.Valid;
import vn.iotstar.dto.CategoryDTO;
import vn.iotstar.entities.CategoryEntity;
import vn.iotstar.entities.UserEntity;
import vn.iotstar.services.ICategoryService;
import vn.iotstar.utils.UploadUtils;
import vn.iotstar.utils.path.Constant;

@Controller("managerCategoryController")
@RequestMapping("manager/categories")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;

    @GetMapping("add")
    public String add(ModelMap model) {
        model.addAttribute("category", new CategoryDTO());
        return "manager/categories/add";
    }

    @PostMapping("add")
    public String addPost(
            @ModelAttribute("category") @Valid CategoryDTO categoryDto,
            BindingResult result,
            @RequestPart("icon") Part file,
            HttpSession session,
            ModelMap model) {

        if (result.hasErrors()) {
            return "manager/categories/add";
        }

        try {
            CategoryEntity category = new CategoryEntity();
            category.setCatename(categoryDto.getCatename());

            UserEntity user = (UserEntity) session.getAttribute("account");
            if (user == null) {
                model.addAttribute("error", "Vui lòng đăng nhập.");
                return "manager/categories/add";
            }

            category.setUserid(user.getId());

            String iconPath = UploadUtils.processUploadFile(file, Constant.DIR + "/category");
            if (iconPath != null) {
                category.setIcon(iconPath);
            }

            categoryService.save(category);
            return "redirect:/manager/categories";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi thêm danh mục.");
            return "manager/categories/add";
        }
    }

    @RequestMapping("")
    public String list(ModelMap model,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "5") int size) {

//        Pageable pageable = PageRequest.of(page, size, Sort.by("cateid").descending());
//        Page<CategoryEntity> categories = categoryService.findAll(pageable);
//
//        model.addAttribute("cateList", categories.getContent()); // dùng trong <c:forEach>
//        model.addAttribute("categories", categories); // dùng cho phân trang
        return "manager/categories/list";
    }

    @GetMapping("delete")
    public String delete(@RequestParam("id") int categoryId, HttpSession session) {
        try {
            categoryService.deleteById(categoryId);
            session.setAttribute("message", "Xóa danh mục thành công.");
        } catch (Exception e) {
            session.setAttribute("message", "Lỗi khi xóa danh mục.");
        }
        return "redirect:/manager/categories";
    }

    @RequestMapping("search")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
        if (name != null && !name.isBlank()) {
            model.addAttribute("cateList", categoryService.findByCatenameContaining(name));
        } else {
            model.addAttribute("cateList", categoryService.findAll());
        }
        return "manager/categories/search";
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
        CategoryEntity entity = categoryService.findById(id).orElse(null);
        if (entity == null) {
            model.addAttribute("error", "Không tìm thấy danh mục.");
            return "redirect:/manager/categories";
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setCateid(entity.getCateid());
        dto.setCatename(entity.getCatename());
        dto.setIcon(entity.getIcon());

        model.addAttribute("category", dto);
        return "manager/categories/edit";
    }

    @PostMapping("edit")
    public String editPost(
            @ModelAttribute("category") @Valid CategoryDTO dto,
            BindingResult result,
            @RequestPart("icon") Part file,
            HttpSession session,
            ModelMap model) throws IOException {

        if (result.hasErrors()) {
            return "manager/categories/edit";
        }

        CategoryEntity category = categoryService.findById(dto.getCateid()).orElse(null);
        if (category == null) {
            model.addAttribute("error", "Không tìm thấy danh mục.");
            return "manager/categories/edit";
        }

        category.setCatename(dto.getCatename());

        UserEntity user = (UserEntity) session.getAttribute("account");
        if (user != null) {
            category.setUserid(user.getId());
        }

        if (file != null && file.getSize() > 0) {
            String iconPath = UploadUtils.processUploadFile(file, Constant.DIR + "/category");
            if (iconPath != null) {
                category.setIcon(iconPath);
            }
        }
        
        if (file != null && file.getSize() > 0) {
            String iconPath = UploadUtils.processUploadFile(file, Constant.DIR + "/category");
            if (iconPath != null) {
                category.setIcon(iconPath);
            }
        }

        categoryService.save(category);
        return "redirect:/manager/categories";
    }
    
    @RequestMapping("searchpaginated")
    public String search(ModelMap model,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);

        // Use catename for sorting since that's the field name in CategoryEntity
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("catename"));
        Page<CategoryEntity> resultPage = null;

        if (StringUtils.hasText(name)) {
            resultPage = categoryService.findByCatenameContaining(name, pageable);
            model.addAttribute("name", name); // Use name parameter instead of catename
        } else {
            resultPage = categoryService.findAll(pageable);
        }

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);

            // Ensure at least 5 page numbers are shown (adjust range if near boundaries)
            if (end - start < 4 && end < totalPages) {
                end = Math.min(end + (4 - (end - start)), totalPages);
                start = Math.max(1, end - 4);
            } else if (end - start < 4 && start > 1) {
                start = Math.max(1, start - (4 - (end - start)));
                end = Math.min(end + (4 - (end - start)), totalPages);
            }

            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("categoryPage", resultPage);
        return "manager/categories/searchpaginated";
    }


}
