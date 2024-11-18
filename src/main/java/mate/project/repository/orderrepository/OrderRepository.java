package mate.project.repository.orderrepository;

import java.util.List;
import mate.project.model.Order;
import mate.project.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.user.id = :userId")
    List<Order> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :orderId")
    Order findByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT oi FROM Order o JOIN o.orderItems oi WHERE o.id = :orderId AND oi.id = :itemId")
    OrderItem findOrderItem(@Param("orderId") Long orderId, @Param("itemId") Long itemId);
}
