package com.pnam.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CourseRequestDTO {

    @NotBlank(message = "Tiêu đề là bắt buộc")
    @Size(max = 200, message = "Tiêu đề tối đa 200 ký tự")
    private String title;

    @Size(max = 65535, message = "Mô tả quá dài")
    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "Giá phải >= 0")
    private BigDecimal price;

    @NotBlank(message = "Trạng thái là bắt buộc")
    private String status;

    @NotNull(message = "Thời lượng là bắt buộc")
    @Positive(message = "Thời lượng phải > 0")
    private BigDecimal durationHours;

    @Size(max = 3, message = "Mã tiền tệ tối đa 3 ký tự")
    private String currency;

    @NotNull(message = "Danh mục là bắt buộc")
    private Long categoryId;

    // ===== Getters & Setters =====
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public String getSlug() {
//        return slug;
//    }
//    public void setSlug(String slug) {
//        this.slug = slug;
//    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(BigDecimal durationHours) {
        this.durationHours = durationHours;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
