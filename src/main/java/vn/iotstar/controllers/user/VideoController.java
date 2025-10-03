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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import jakarta.validation.Valid;

import vn.iotstar.entities.VideoEntity;
import vn.iotstar.entities.CategoryEntity;
import vn.iotstar.entities.UserEntity;
import vn.iotstar.dto.VideoDTO;
import vn.iotstar.services.IVideoService;
import vn.iotstar.services.ICategoryService;
import vn.iotstar.utils.UploadUtils;
import vn.iotstar.utils.path.Constant;

@Controller("userVideoController")
@RequestMapping("user/videos")
public class VideoController {

	@Autowired
	private IVideoService videoService;

	@Autowired
	private ICategoryService categoryService;

	@GetMapping("add")
	public String add(ModelMap model) {
		model.addAttribute("video", new VideoDTO());
		model.addAttribute("categories", categoryService.findAll());
		return "user/videos/add";
	}

	@PostMapping("add")
	public String addPost(@ModelAttribute("video") @Valid VideoDTO dto, BindingResult result,
			@RequestPart(name = "filePoster", required = false) Part file, HttpSession session, ModelMap model) {

		model.addAttribute("categories", categoryService.findAll());

		if (result.hasErrors()) {
			return "user/videos/add";
		}

		try {
			VideoEntity video = new VideoEntity();
			video.setTitle(dto.getTitle());
			video.setDescription(dto.getDescription());

			UserEntity user = (UserEntity) session.getAttribute("account");
			if (user == null) {
				model.addAttribute("error", "Vui lòng đăng nhập trước khi thêm video.");
				return "user/videos/add";
			}

			video.setUserid(user.getId());
			video.setActive(dto.isActive());
			video.setViews(0);

			// Category
			if (dto.getCategoryId() != null) {
				categoryService.findById(dto.getCategoryId()).ifPresent(video::setCategory);
			}

			// Upload poster
			if (file != null && file.getSize() > 0) {
				String fileName = file.getSubmittedFileName();
				if (!fileName.matches(".+\\.(jpg|jpeg|png|gif)$")) {
					model.addAttribute("error", "File phải là ảnh jpg, jpeg, png hoặc gif.");
					return "user/videos/add";
				}
				String posterPath = UploadUtils.processUploadFile(file, Constant.DIR + "/video");
				if (posterPath != null) {
					video.setPoster(posterPath);
				}
			}

			videoService.save(video);
			session.setAttribute("message", "Thêm video thành công!");
			return "redirect:/user/videos";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Có lỗi xảy ra khi thêm video: " + e.getMessage());
			return "user/videos/add";
		}
	}

	@GetMapping("edit")
	public String editForm(@RequestParam("videoid") int videoId, ModelMap model) {
		VideoEntity video = videoService.findById(videoId).orElse(null);
		if (video == null) {
			return "redirect:/user/videos";
		}

		VideoDTO dto = new VideoDTO();
		dto.setVideoid(video.getVideoid());
		dto.setTitle(video.getTitle());
		dto.setDescription(video.getDescription());
		dto.setPoster(video.getPoster());
		dto.setActive(video.isActive());
		dto.setViews(video.getViews());
		dto.setUserid(video.getUserid());
		dto.setCategoryId(video.getCategory() != null ? video.getCategory().getCateid() : null);

		model.addAttribute("video", dto);
		model.addAttribute("categoryList", categoryService.findAll());

		return "user/videos/edit";
	}

	@PostMapping("edit")
	public String editPost(@ModelAttribute("video") @Valid VideoDTO dto, BindingResult result,
			@RequestPart(name = "filePoster", required = false) Part file, HttpSession session, ModelMap model) {

		model.addAttribute("categoryList", categoryService.findAll());

		// Lấy video từ DB trước để giữ poster cũ khi có lỗi validation
		VideoEntity video = videoService.findById(dto.getVideoid()).orElse(null);
		if (video == null) {
			model.addAttribute("error", "Không tìm thấy video để cập nhật.");
			return "user/videos/edit";
		}

		// Set lại poster cũ vào DTO để hiển thị khi có lỗi
		dto.setPoster(video.getPoster());
		model.addAttribute("video", dto);

		// Validate
		if (result.hasErrors()) {
			return "user/videos/edit";
		}

		try {
			// Cập nhật thông tin cơ bản
			video.setTitle(dto.getTitle());
			video.setDescription(dto.getDescription());
			video.setActive(dto.isActive());
			video.setViews(dto.getViews());

			// Cập nhật user
			UserEntity user = (UserEntity) session.getAttribute("account");
			if (user != null) {
				video.setUserid(user.getId());
			}

			// Cập nhật category
			if (dto.getCategoryId() != null) {
				categoryService.findById(dto.getCategoryId()).ifPresent(video::setCategory);
			} else {
				video.setCategory(null);
			}

			// Xử lý poster mới
			if (file != null && file.getSize() > 0) {
				String fileName = file.getSubmittedFileName();

				if (!fileName.matches(".+\\.(jpg|jpeg|png|gif)$")) {
					model.addAttribute("error", "File phải là ảnh jpg, jpeg, png hoặc gif.");
					return "user/videos/edit";
				}

				// Xóa poster cũ nếu có
				if (video.getPoster() != null && !video.getPoster().isEmpty()) {
					File oldFile = new File(Constant.DIR + "/" + video.getPoster());
					if (oldFile.exists()) {
						oldFile.delete();
					}
				}

				// Lưu poster mới
				String posterPath = UploadUtils.processUploadFile(file, Constant.DIR + "/video");
				if (posterPath != null) {
					video.setPoster(posterPath);
				}
			}

			// Lưu video
			videoService.save(video);
			session.setAttribute("message", "Cập nhật video thành công!");
			return "redirect:/user/videos";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Có lỗi xảy ra khi cập nhật video: " + e.getMessage());
			return "user/videos/edit";
		}
	}

	@RequestMapping("")
	public String list(ModelMap model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {

		if (page < 0)
			page = 0;
		Pageable pageable = PageRequest.of(page, size, Sort.by("title").descending());
		Page<VideoEntity> videos = videoService.findAll(pageable);

		model.addAttribute("videoList", videos.getContent());
		model.addAttribute("videos", videos);
		return "user/videos/list";
	}

	@GetMapping("delete")
	public String delete(@RequestParam("videoid") int videoId, HttpSession session) {
		try {
			VideoEntity video = videoService.findById(videoId).orElse(null);
			if (video != null && video.getPoster() != null) {
				// Xóa file ảnh trước khi xóa record
				File file = new File(Constant.DIR + "/" + video.getPoster());
				if (file.exists()) {
					file.delete();
				}
			}
			videoService.deleteById(videoId);
			session.setAttribute("message", "Xóa video thành công.");
		} catch (Exception e) {
			session.setAttribute("message", "Lỗi khi xóa video: " + e.getMessage());
		}
		return "redirect:/user/videos";
	}

	@RequestMapping("search")
	public String search(ModelMap model, @RequestParam(name = "title", required = false) String title) {
		if (title != null && !title.isBlank()) {
			model.addAttribute("videoList", videoService.findByTitleContaining(title));
		} else {
			model.addAttribute("videoList", videoService.findAll());
		}
		return "user/videos/search";
	}

	@GetMapping("image")
	public void downloadImage(@RequestParam("fname") String fileName, HttpServletResponse resp) throws IOException {
		if (fileName == null || fileName.isEmpty()) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		File file = new File(Constant.DIR + "/" + fileName);
		if (file.exists()) {
			String contentType = Files.probeContentType(file.toPath());
			resp.setContentType(contentType != null ? contentType : "image/jpeg");
			Files.copy(file.toPath(), resp.getOutputStream());
			resp.flushBuffer();
		} else {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}