package fedoseev.tasks.system.service;

import fedoseev.tasks.system.models.CreatedTaskRequest;
import fedoseev.tasks.system.models.TaskPriority;
import fedoseev.tasks.system.models.TaskStatus;
import fedoseev.tasks.system.models.Task;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService{

  private  final Map<Long,Task> taskMap =  new HashMap<>();

  private final AtomicLong generatedId = new AtomicLong(1);

  private static final Logger log = LoggerFactory.getLogger(TaskService.class);


    public Task getTaskById(Long id) {
        if (id == null || id <= 0) {
            throw new NoSuchElementException("Task id must be positive");
        }

        Task task = taskMap.get(id);

        if (task == null) {
            throw new NoSuchElementException("Task not found with id: " + id);
        }

        return task;
    }

  public List<Task> getAllTasks(){
      return new ArrayList<>(taskMap.values());
  }

    @PostConstruct
    public void initTasks() {
        Task task1 = new Task(
                getLong(),
                2L,
                2L,
                TaskStatus.CREATED,
                LocalDateTime.of(2026, 7, 10, 12, 0),
                LocalDateTime.of(2026, 7, 15,15,5),
                TaskPriority.HIGH
        );

        Task task2 = new Task(
                getLong(),
                3L,
                4L,
                TaskStatus.CREATED,
                LocalDateTime.of(2026, 7, 6, 10, 30),
                LocalDateTime.of(2026, 7, 18,13,7),
                TaskPriority.LOW
        );

        Task task3 = new Task(
                getLong(),
                4L,
                5L,
                TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2026, 7, 6, 14, 15),
                LocalDateTime.of(2026, 7, 18,22,0),
                TaskPriority.MEDIUM
        );

        taskMap.put(task1.id(), task1);
        taskMap.put(task2.id(), task2);
        taskMap.put(task3.id(), task3);

        log.info("Task storage initialized with {} tasks", taskMap.size());
    }


    public Task createdTask(
            @RequestBody CreatedTaskRequest request){

        if (request.creatorId() == null){
            throw new NoSuchElementException("creator id must not null");
        }

        if (request.assignedUserId() == null){
            throw new NoSuchElementException("assignedUserId must not null");
        }

        if (request.createDateTime() == null){
            throw new NoSuchElementException("createDateTime must not null");
        }

        if (request.deadlineDate() == null){
            throw new NoSuchElementException("deadlineDate must not null");
        }

        if (request.priority() == null){
            throw new NoSuchElementException("priority must not null");
        }


        Long id = generatedId.getAndIncrement();

        Task generatedTask = new Task(
                id,
                request.creatorId(),
                request.assignedUserId(),
                TaskStatus.CREATED,
                request.createDateTime(),
                request.deadlineDate(),
                request.priority()
        );
        log.info("Created task information id={} and CreatedTaskRequest={} ",id,request);
        taskMap.put(id, generatedTask);

        return generatedTask;
    }

    public Task updatedTask(
            @PathVariable Long id,
            @RequestBody Task taskToUpdate) {

        log.info("Called method updatedTask id={} and taskToUpdate={}",id,taskToUpdate);


        var existingTask = taskMap.get(id);

// Проверка 1: задача вообще существует?
        if (existingTask == null) {
            throw new NoSuchElementException("Task id=" + id + " not found");
        }

// Проверка 2: задачу можно редактировать (не DONE, кроме перевода в IN_PROGRESS)?
        if (existingTask.status() == TaskStatus.DONE
                && taskToUpdate.status() != TaskStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Cannot update task id=" + id + " because it is already DONE");
        }

        Task updatedTask = new Task(
                id,
                taskToUpdate.creatorId(),
                taskToUpdate.assignedUserId(),
                taskToUpdate.status(),
                taskToUpdate.createDateTime(),
                taskToUpdate.deadlineDate(),
                taskToUpdate.priority()
        );
        taskMap.put(id,updatedTask);
        return updatedTask;
    }

    public void deletedById(Long id) {
        if (!taskMap.containsKey(id)){
            throw new NoSuchElementException("not found by id="+ id);
        }
        log.info("Deleted by id={}",id);
        taskMap.remove(id);
    }
    private Long getLong(){
        return generatedId.getAndIncrement();
    }



}
