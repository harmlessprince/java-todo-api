package todoapplication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import todoapplication.models.Task;
import todoapplication.repositories.ItemRepository;
import todoapplication.repositories.TaskRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private TaskRepository taskRepository;
    private ItemRepository itemRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository, ItemRepository itemRepository) {
        this.taskRepository = taskRepository;
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Task>> index(Pageable pageable) {
        return ResponseEntity.ok(taskRepository.findAll(pageable));
    }

    /**
     * Gets users by id.
     *
     * @param id the task id
     * @return the tasks by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskRepository.getById(id);
        return ResponseEntity.ok().body(task);
    }

    /**
     * Create Task task.
     *
     * @param task the task
     * @return the task
     */
    @PostMapping()
    public ResponseEntity<Task> create(@Valid @RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{:id}").buildAndExpand(savedTask.getTaskId()).toUri();
        return ResponseEntity.created(location).body(savedTask);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task task) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) return ResponseEntity.unprocessableEntity().build();
        task.setTaskId(optionalTask.get().getTaskId());
        taskRepository.save(task);
        System.out.println(task);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteTaskById(@PathVariable Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) ResponseEntity.notFound().build();
        deleteTaskInTransaction(optionalTask.get());
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public void deleteTaskInTransaction(Task task) {
        itemRepository.deleteByTaskId(task.getTaskId());
        taskRepository.delete(task);
    }
}
