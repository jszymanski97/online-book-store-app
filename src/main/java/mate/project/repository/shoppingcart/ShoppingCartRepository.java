package mate.project.repository.shoppingcart;

import java.util.Optional;
import mate.project.model.ShoppingCart;
import mate.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUser(User user);

    Optional<ShoppingCart> findAllByUserId(Long userId);
}
