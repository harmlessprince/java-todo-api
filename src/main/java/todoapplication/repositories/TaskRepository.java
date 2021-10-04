package todoapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import todoapplication.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
