package ca.gbc.productservice.controller;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // It will handle HTTP requests and responses in JSON Format
@RequiredArgsConstructor
@RequestMapping("/api/product") //base URL. all the endpoints in the class will start with this.
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //201 - Created
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        //tells Spring to take JSON body from the request and map it to a ProductRequest object.This object contains
        // the details of new product
        ProductResponse createdProduct = productService.createProduct(productRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/products" + createdProduct.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdProduct);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK) // 200 - OK
    public List<ProductResponse> getAllProducts(){
//        try{
//            Thread.sleep(5000);
//        }catch(InterruptedException e){
//            throw new RuntimeException(e);
//        }
        return productService.getAllProducts(); //It returns a list of ProductResponse objects, which is automatically converted to JSON and sent in the response body.
    }

    //http://localhost:8082/api/product
    @PutMapping("/{productId}") //When a client sends a PUT request to /api/product/{productId}, this method gets
    // called.
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> updateProduct(@PathVariable("productId") String productId,
                                           @RequestBody ProductRequest productRequest){
       String updateProductId =  productService.updateProduct(productId, productRequest);

       //sets the location header attribute
        HttpHeaders headers  = new HttpHeaders();
        headers.add("Location", "/api/product" + updateProductId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") String productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}