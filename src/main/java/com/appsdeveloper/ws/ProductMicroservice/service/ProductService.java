package com.appsdeveloper.ws.ProductMicroservice.service;

import com.appsdeveloper.ws.ProductMicroservice.model.CreateProductRestModel;

public interface ProductService {

    String createProduct(CreateProductRestModel productRestModel) throws Exception;
}
