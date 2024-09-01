package com.bunnyMax.productTrading.controller.data.resp;

import com.bunnyMax.productTrading.vo.ProductVo;
import lombok.Data;

import java.util.List;

@Data
public class ProductListResp {

    private long pageNum;
    private long pageSize;
    private long currentSize;
    private long total;
    private long totalPage;
    private List<ProductVo> productList;

}
