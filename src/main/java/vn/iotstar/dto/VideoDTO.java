package vn.iotstar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VideoDTO {

    private int videoid;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 100, message = "Tiêu đề không được vượt quá 100 ký tự")
    private String title;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 10, max = 1000, message = "Mô tả phải có từ 10 đến 1000 ký tự")
    private String description;

    private String poster; // tên file sau khi upload

    private Integer categoryId; // id category được chọn từ dropdown

    private boolean active; // checkbox active

    private int views; // mặc định 0
    private int userid; // gán từ session
}
