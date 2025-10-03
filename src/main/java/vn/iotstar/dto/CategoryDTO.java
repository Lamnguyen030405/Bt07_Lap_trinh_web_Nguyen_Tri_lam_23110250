package vn.iotstar.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {

    private Integer cateid; // Dùng cho update

    @NotBlank(message = "Tên danh mục không được để trống")
    private String catename;

    private String icon;
}
