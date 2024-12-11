package ca.gbc.productservice.service;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import java.util.List;
@Service //service component - It will automatically instantiate and inject dependencies
@Slf4j //By Lombok - for logger
@RequiredArgsConstructor //Creates automatically a constructor for final fields
public class ProductServicempl implements ProductService{

    private final ProductRepository productRepository; //repostory interface that provides access to MongoDb
    private final MongoTemplate mongoTemplate; //Spring data MongoDB helper class used to interact with MongoDB. More
    // flexible than Product Repository
    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {

        log.debug("Creating a new Product {}", productRequest.name()); //login debug mode, deployment. It creates a
        // new Product using ProductRequest data

        //creats a Product entity from ProductRequest DTO
        Product product = Product.builder().name(productRequest.name()).
                        description(productRequest.description()).
                        price(productRequest.price()).
                        build();

        //persist a product to the database
        productRepository.save(product);

        //confirms that product is saved successfully
        log.info("Product {} is saved", product.getId()); //production environment

        //convert the save product to ProductResponse DTO and return it for the client
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());

    }

    @Override
    public List<ProductResponse> getAllProducts() {
        //fetches all products from database and return the list of ProductResponse DTOs
        log.debug("Returning a list of products"); //debug message
        //retrives all products from the MongoDb collection
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> mapToProductResponse(product)).toList(); // returns the list of the
        // products. It will convert each product into the ProductResponse and add it to the list

    }

    //maps a Product(car) entity to a ProductResponse(Car Picture) DTO, allowing to duplicate database model from the
    // data you want to expose to the client
    private ProductResponse mapToProductResponse(Product product){
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }


    @Override
    public String updateProduct(String id, ProductRequest productRequest) {
        log.debug("Updating a product with id {}", id);
        //id from actual collection is equal to id passed in the arguments

        //creates a query to find a product by ID
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        //find the product by ID using Mongotemplate
        Product product = mongoTemplate.findOne(query, Product.class);

        if(product != null){
            //update the product fields with new values from ProductRequest
            product.setDescription(productRequest.description());
            product.setPrice(productRequest.price());
            product.setName(productRequest.name());

            return productRepository.save(product).getId(); //save the updated product to the database and return its
            // ID
        }
        return id;
    }

    @Override
    public void deleteProduct(String id) {
        log.debug("Deleting a product with id {}", id);
        productRepository.deleteById(id);
    }
}
