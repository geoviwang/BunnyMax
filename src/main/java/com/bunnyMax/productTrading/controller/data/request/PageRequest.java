package com.bunnyMax.productTrading.controller.data.request;

import com.bunnyMax.productTrading.entity.Product;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class PageRequest {
    @Min(1)
    private int pageNum;
    @Min(1)
    private int pageSize;
}
