package todoapplication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import todoapplication.models.Item;
import todoapplication.models.Task;
import todoapplication.repositories.ItemRepository;
import todoapplication.repositories.TaskRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/items")
public class ItemController {
    private TaskRepository taskRepository;
    private ItemRepository itemRepository;

    @Autowired
    public ItemController(TaskRepository taskRepository, ItemRepository itemRepository) {
        this.taskRepository = taskRepository;
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public Page<Item> getAllItemsByTaskId(@PathVariable(value = "taskId") Long taskId, Pageable pageable) {
        return itemRepository.findByTaskTaskId(taskId, pageable);
    }

    @PostMapping
    public ResponseEntity<Item> create(@PathVariable(value = "taskId") Long taskId, @Valid @RequestBody Item item) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (!optionalTask.isPresent()) ResponseEntity.notFound().build();
        item.setTask(optionalTask.get());
        Item savedItem = itemRepository.save(item);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedItem.getItemId()).toUri();
        return ResponseEntity.created(location).body(savedItem);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Item> update(@PathVariable(value = "taskId") Long taskId, @PathVariable(value = "itemId") Long itemId, @Valid @RequestBody Item item) {
        if (!taskRepository.existsById(taskId)) ResponseEntity.notFound().build();
        Optional<Item> updatedItem = itemRepository.findById(itemId).map(element -> {
            element.setItem(item.getItem());
            element.setComplete(item.getComplete());
            return itemRepository.save(element);
        });
        updatedItem.orElseThrow();
        return ResponseEntity.ok().body(updatedItem.get());
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> destroy(@PathVariable(value = "taskId") Long taskId, @PathVariable(value = "itemId") Long itemId) {
        if (!taskRepository.existsById(taskId)) ResponseEntity.notFound().build();
        return itemRepository.findByItemIdAndTaskTaskId(itemId, taskId).map(element -> {
            itemRepository.delete(element);
            return ResponseEntity.ok().build();
        }).orElseThrow();
    }
}
