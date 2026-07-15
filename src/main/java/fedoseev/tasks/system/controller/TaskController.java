package fedoseev.tasks.system.controller;

import fedoseev.tasks.system.models.task.*;
import fedoseev.tasks.system.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
          @Valid @RequestBody CreatedTaskRequest createdTaskRequest){
        log.info("Call method createdTask");
      return ResponseEntity.status(201)
              .body(taskService.createdTask(createdTaskRequest));
  }

  @PostMapping("/{id}/start")
  public ResponseEntity<Task> startTask(
          @PathVariable Long id
  ){
        log.info("Call method startTask id{}",id);
        return ResponseEntity.ok(taskService.startTask(id));
  }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(
            @PathVariable Long id
    ){
        log.info("Call method completeTask id{}",id);
        return ResponseEntity.ok(taskService.completeTask(id));
    }

  @PutMapping("/{id}")
  public ResponseEntity<Task> updatedTaskById(
          @PathVariable Long id,
          @RequestBody  Task taskToUpdate
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

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(
            @RequestParam(required = false) Long creatorId,
            @RequestParam(required = false) Long assignedUserId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNum
    ) {
        TaskSearchFilter filter = new TaskSearchFilter(creatorId, assignedUserId, status, priority, pageSize, pageNum);
        return ResponseEntity.ok(taskService.searchTasks(filter));
    }




}



