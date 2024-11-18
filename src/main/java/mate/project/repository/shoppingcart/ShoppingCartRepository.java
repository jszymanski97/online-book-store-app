package mate.project.repository.shoppingcart;

import java.util.Optional;
import mate.project.model.ShoppingCart;
import mate.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUser(User user);

    Optional<ShoppingCart> findAllByUserId(Long userId);

    @Query("SELECT sc FROM ShoppingCart sc LEFT JOIN FETCH sc.cartItems WHERE sc.user = :user")
    Optional<ShoppingCart> findByUserWithCartItems(@Param("user") User user);
}
