package gift.Service;

import gift.DTO.RequestProductDTO;
import gift.DTO.RequestProductPostDTO;
import gift.Exception.CategoryNotFoundException;
import gift.Exception.ProductNotFoundException;
import gift.Model.Entity.Category;
import gift.Model.Entity.Option;
import gift.Model.Entity.Product;
import gift.Repository.CategoryRepository;
import gift.Repository.OptionRepository;
import gift.Repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;


    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository  = categoryRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage;
    }

    @Transactional
    public void addProduct(RequestProductPostDTO requestProductPostDTO) {
        Category category = categoryRepository.findById(requestProductPostDTO.categoryId())
                .orElseThrow(()-> new CategoryNotFoundException("매칭되는 카테고리가 없습니다"));
        Product product = new Product(requestProductPostDTO.name(), requestProductPostDTO.price(), requestProductPostDTO.imageUrl(), category);
        productRepository.save(product);
        optionRepository.save(new Option(requestProductPostDTO.optionName(), requestProductPostDTO.optionQuantity(), product));
    }

    @Transactional(readOnly = true)
    public Product selectProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("매칭되는 product가 없습니다"));
        return product;
    }

    @Transactional
    public void editProduct(long id, RequestProductDTO requestProductDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("매칭되는 product가 없습니다"));
        Category category = categoryRepository.findById(requestProductDTO.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("매칭되는 카테고리가 없습니다"));
        product.update(requestProductDTO.name(), requestProductDTO.price(), requestProductDTO.imageUrl(), category);
    }

    @Transactional
    public void deleteProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("매칭되는 product가 없습니다"));
        optionRepository.deleteByProduct(product);
        productRepository.deleteById(product.getId());
    }


}