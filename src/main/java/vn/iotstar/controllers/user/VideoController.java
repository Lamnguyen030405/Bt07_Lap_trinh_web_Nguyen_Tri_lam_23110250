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
import vn.iotstar.entities.VideoEntity;
import vn.iotstar.entities.UserEntity;
import vn.iotstar.services.IVideoService;
import vn.iotstar.utils.UploadUtils;
import vn.iotstar.utils.path.Constant;

@Controller("userVideoController")
@RequestMapping("user/videos")
public class VideoController {

    @Autowired
    IVideoService videoService;

    @GetMapping("add")
    public String add(ModelMap model) {
        return "user/videos/add";
    }

    @PostMapping("add")
    public String addPost(@RequestParam("title") String title,
                          @RequestParam("description") String description, // Add description parameter
                          @RequestPart("poster") Part file,
                          HttpSession session,
                          ModelMap model) {

        try {
            VideoEntity video = new VideoEntity();
            video.setTitle(title);
            video.setDescription(description); // Set description from form

            UserEntity user = (UserEntity) session.getAttribute("account");
            if (user == null) {
                model.addAttribute("error", "Vui lòng đăng nhập trước khi thêm video.");
                return "user/videos/add";
            }

            video.setUserid(user.getId());
            video.setActive(false); // Default value for active
            video.setViews(0); // Default value for views

            String posterPath = UploadUtils.processUploadFile(file, Constant.DIR + "/video");
            if (posterPath != null) {
                video.setPoster(posterPath);
            }

            videoService.save(video);
            return "redirect:/user/videos";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi thêm video: " + e.getMessage());
            return "user/videos/add";
        }
    }

    @RequestMapping("")
    public String list(ModelMap model,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("title").descending());
        Page<VideoEntity> videos = videoService.findAll(pageable);

        model.addAttribute("videoList", videos.getContent()); // dùng trong <c:forEach>
        model.addAttribute("videos", videos); // dùng cho phân trang
        return "user/videos/list";
    }

    @GetMapping("delete")
    public String delete(@RequestParam("id") int videoId, HttpSession session) {
        try {
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

    @GetMapping("/image")
    public void download(@RequestParam("fname") String fileName, HttpServletResponse resp) throws IOException {
        File file = new File(Constant.DIR + "/video/" + fileName);
        if (file.exists()) {
            Files.copy(file.toPath(), resp.getOutputStream());
            resp.flushBuffer();
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @GetMapping("edit")
    public String editForm(@RequestParam("id") int id, ModelMap model) {
        VideoEntity video = videoService.findById(id).orElse(null);
        if (video == null) {
            model.addAttribute("error", "Không tìm thấy video.");
            return "redirect:/user/videos";
        }
        model.addAttribute("video", video);
        return "user/videos/edit";
    }
    
    @PostMapping("edit")
    public String editPost(
            @RequestParam("id") int id,
            @RequestParam("title") String title,
            @RequestParam("description") String description, // Add description parameter
            @RequestPart("poster") Part file,
            HttpSession session,
            ModelMap model) {

        try {
            VideoEntity video = videoService.findById(id).orElse(null);
            if (video == null) {
                model.addAttribute("error", "Không tìm thấy video để cập nhật.");
                return "user/videos/edit";
            }

            video.setTitle(title);
            video.setDescription(description); // Update description

            UserEntity user = (UserEntity) session.getAttribute("account");
            if (user != null) {
                video.setUserid(user.getId());
            }

            if (file != null && file.getSize() > 0) {
                String posterPath = UploadUtils.processUploadFile(file, Constant.DIR + "/video");
                if (posterPath != null) {
                    video.setPoster(posterPath);
                }
            }

            videoService.save(video);
            return "redirect:/user/videos";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật video: " + e.getMessage());
            return "user/videos/edit";
        }
    }
}