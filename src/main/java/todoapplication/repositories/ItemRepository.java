package todoapplication.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import todoapplication.models.Item;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM items item WHERE item.task.id = ?1")
    void deleteByTaskId(Long taskId);

    Page<Item> findByTaskTaskId(Long taskId, Pageable pageable);
    Optional<Item> findByItemIdAndTaskTaskId(Long itemId, Long taskId);
}
