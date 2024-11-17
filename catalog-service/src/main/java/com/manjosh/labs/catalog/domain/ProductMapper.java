package com.manjosh.labs.catalog.domain;

class ProductMapper {
    static Product toProduct(ProductEntity entity) {
        return new Product(
                entity.getCode(), entity.getName(), entity.getDescription(), entity.getImageUrl(), entity.getPrice());
    }
}
