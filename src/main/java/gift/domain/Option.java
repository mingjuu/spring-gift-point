package gift.domain;

import gift.dto.request.OptionRequestDto;
import gift.exception.customException.OptionQuantityNotMinusException;
import gift.utils.TimeStamp;
import jakarta.persistence.*;

@Entity
public class Option extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    public Option() {
    }

    public Option(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void addProduct(Product product){
        this.product = product;
        product.getOptions().add(this);
    }

    public void update(OptionRequestDto optionRequestDto){
        this.name = optionRequestDto.optionName();
        this.quantity = optionRequestDto.optionQuantity();
    }

    public void updateQuantity(int quantity){

        if(this.quantity < quantity){
            throw new OptionQuantityNotMinusException();
        }

        this.quantity = this.quantity - quantity;
    }
}