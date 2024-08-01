package gift.wish.service;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.product.entity.Product;
import gift.product.repository.ProductJpaRepository;
import gift.user.entity.User;
import gift.user.repository.UserJpaRepository;
import gift.wish.dto.request.CreateWishRequest;
import gift.wish.dto.request.UpdateWishRequest;
import gift.wish.dto.response.WishResponse;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductJpaRepository productRepository;
    private final UserJpaRepository userRepository;

    public WishService(WishRepository wishRepository, ProductJpaRepository productRepository,
        UserJpaRepository userRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<WishResponse> getWishes(Long userId, Pageable pageable) {
        Page<Wish> wishes = wishRepository.findByUserId(userId, pageable);
        validateWishPage(wishes);
        return wishes.map(WishResponse::from);
    }

    @Transactional
    public WishResponse createWish(Long userId, CreateWishRequest request) {
        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Wish wish = new Wish(user, product, request.quantity());

        return WishResponse.from(wishRepository.save(wish));
    }

    @Transactional
    public void updateWishes(List<UpdateWishRequest> requests) {
        for (UpdateWishRequest request : requests) {
            updateWish(request);
        }
    }

    @Transactional
    public void deleteWish(Long id) {
        wishRepository.deleteById(id);
    }

    @Transactional
    protected void updateWish(UpdateWishRequest request) {
        Wish wish = getWish(request.id());
        wish.changeQuantity(request);
        if (wish.isQuantityUnderZero()) {
            wishRepository.delete(wish);
        }
    }

    @Transactional(readOnly = true)
    protected Wish getWish(Long id) {
        return wishRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.WISH_NOT_FOUND));
    }

    private void validateWishPage(Page<Wish> wishes) {
        if (wishes == null || wishes.isEmpty()) {
            throw new CustomException(ErrorCode.WISH_NOT_FOUND);
        }
    }

}