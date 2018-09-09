package com.onesport.organicappserver.service;

import com.onesport.organicappserver.entity.ProductsEntity;
import com.onesport.organicappserver.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {
    @Autowired
    private ProductsRepository _productsRepository;

    public Integer saveProducts(ProductsEntity products){
        ProductsEntity _products =  _productsRepository.save(products);
        if(_products !=null){
            return _products.getProductId();
        }
        return null;
    }

    public List<ProductsEntity> getAllProducts(ProductsEntity.ProductAvailability status){
       return _productsRepository.getAllProducts(status);
    }
}
