package sample.cafekiosk.spring.api.service.product;

import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    // 동시성 이슈
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        // productNumber
        // 001 002 003 004
        // DB 에서 마지막  저장된 Product의 상품 번호를 읽어와서 +1
        // 009 -> 010
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if(latestProductNumber == null){
            return "001";
        }

        int latestProductNumberInt = Integer.valueOf(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        // 9 --> 009 10 -> 010
        return String.format("%03d", nextProductNumberInt);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(forDisplay());
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }


}