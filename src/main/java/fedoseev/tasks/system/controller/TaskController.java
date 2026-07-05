package fedoseev.tasks.system.controller;

import fedoseev.tasks.system.models.CreatedTaskRequest;
import fedoseev.tasks.system.models.Task;
import fedoseev.tasks.system.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/tasks")
public class TaskController {

  private static final Logger log = LoggerFactory.getLogger(TaskController.class);

  private final  TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

  @GetMapping("/{id}")
  public ResponseEntity<Task> getTaskById(
          @PathVariable Long id) {
      log.info("Call method getTaskById id={}",id);

      return ResponseEntity.ok( taskService.getTaskById(id));
  }

  @GetMapping()
  public ResponseEntity<List<Task>> getAllTasks(){
      log.info("Call method getAllTask");

      return ResponseEntity.ok(taskService.getAllTasks());
  }

  @PostMapping()
  public ResponseEntity<Task> createdTask(
         @RequestBody CreatedTaskRequest createdTaskRequest){
        log.info("Call method createdTask");
      return ResponseEntity.status(201)
              .body(taskService.createdTask(createdTaskRequest));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Task> updatedTaskById(
          @PathVariable Long id,
          @RequestBody Task taskToUpdate
  ){
      return ResponseEntity.ok(taskService.updatedTask(id,taskToUpdate));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletedTask(
          @PathVariable Long id
  ){
        log.info("called method deletedTask by id={}",id);
      taskService.deletedById(id);
    return ResponseEntity.ok()
            .build();
  }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException e) {
        log.warn("Not found: {}", e.getMessage());
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public  ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        log.warn("Illegal Argument: {}", e.getMessage());
        return ResponseEntity.status(400).body(e.getMessage());
    }
}



