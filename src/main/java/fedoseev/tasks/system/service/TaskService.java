package fedoseev.tasks.system.service;

import fedoseev.tasks.system.exceptions.InvalidTaskIdException;
import fedoseev.tasks.system.exceptions.TaskAlreadyCompletedException;
import fedoseev.tasks.system.exceptions.TaskCannotBeStartedException;
import fedoseev.tasks.system.exceptions.TaskNotFoundException;
import fedoseev.tasks.system.models.task.*;
import fedoseev.tasks.system.repositories.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService{

 @Autowired
 private TaskRepository taskRepository;

 @Autowired
 private TaskMapper taskMapper;

  private static final Logger log = LoggerFactory.getLogger(TaskService.class);


    public Task getTaskById(Long id) {

        if (id == null || id <= 0) {
            throw new InvalidTaskIdException("Task id must be positive");
        }
        TaskEntity  entity = getTaskEntityOrThrow(id);
        return  taskMapper.toModel(entity);
    }

  public List<Task> getAllTasks(){

        return taskRepository.findAll()
                .stream().map(taskMapper::toModel).toList();
  }

    public Task createdTask(
            @RequestBody CreatedTaskRequest request){

        TaskEntity newTask = new TaskEntity();

        newTask.setCreatorId(request.creatorId());

        newTask.setAssignedUserId(request.assignedUserId());

        newTask.setStatus(TaskStatus.CREATED);

        newTask.setCreateDateTime(request.createDateTime());

        newTask.setDeadlineDate(request.deadlineDate());

        newTask.setPriority(request.priority());

        TaskEntity savedTask = taskRepository.save(newTask);
        log.info("Created task information id={} and CreatedTaskRequest={} ",savedTask.getId(),request);

        return taskMapper.toModel(savedTask);
    }

    public Task updatedTask(
            @PathVariable Long id,
            @RequestBody TaskUpdated taskToUpdate) {

        log.info("Called method updatedTask id={} and taskToUpdate={}",id,taskToUpdate);

        TaskEntity existingTask =  getTaskEntityOrThrow(id);

        if  (existingTask.getStatus() == TaskStatus.DONE ){
            throw new TaskAlreadyCompletedException("Task already completed");
        }
        if (existingTask.getStatus() == TaskStatus.IN_PROGRESS){
            checkAssignedUserId(taskToUpdate.assignedUserId());
        }
        existingTask.setAssignedUserId(taskToUpdate.assignedUserId());
        existingTask.setPriority(taskToUpdate.priority());
        existingTask.setDeadlineDate(taskToUpdate.deadlineDate());

        var savedUpdated = taskRepository.save(existingTask);
        log.info("Updated task by id={} and Entity={}",id,taskToUpdate);
        return taskMapper.toModel(savedUpdated);
    }

    public void deletedById(Long id) {
        getTaskEntityOrThrow(id);
        taskRepository.deleteById(id);
        log.info("Deleted by id{}",id);
    }

    public Task startTask(Long id) {
        TaskEntity taskEntity = getTaskEntityOrThrow(id);
        if (taskEntity.getAssignedUserId() == null){
            throw new TaskCannotBeStartedException("Task id=" + id + " cannot be started from status " + taskEntity.getStatus());
        }
        if (taskEntity.getStatus() != TaskStatus.CREATED){
            throw new TaskCannotBeStartedException("Cannot start task because it is already in progress");
        }
        checkAssignedUserId(taskEntity.getAssignedUserId());
        taskEntity.setStatus(TaskStatus.IN_PROGRESS);

        var saved = taskRepository.save(taskEntity);
        return taskMapper.toModel(saved);
    }

    public Task completeTask(Long id) {
        TaskEntity taskEntity =  getTaskEntityOrThrow(id);

        if (taskEntity.getStatus() == (TaskStatus.DONE)){
            throw  new TaskAlreadyCompletedException("Cannot complete task");
        }

       if (taskEntity.getAssignedUserId() == null ){
           throw new IllegalArgumentException("AssignedUserId must be not null");
       }
       if ( taskEntity.getDeadlineDate() == null){
           throw new IllegalArgumentException("DeadLineDate must be not null");
       }
       taskEntity.setStatus(TaskStatus.DONE);
       taskEntity.setDoneDateTime(LocalDateTime.now());

       var save = taskRepository.save(taskEntity);

        return taskMapper.toModel(save);
    }

    public List<Task> searchTasks(TaskSearchFilter filter) {
        Pageable pageable = PageRequest.of(filter.pageNum(), filter.pageSize());

        Page<TaskEntity> page = taskRepository.searchTasks(
                filter.creatorId(), filter.assignedUserId(), filter.status(), filter.priority(), pageable
        );

        return page.stream().map(taskMapper::toModel).toList();
    }

    private TaskEntity getTaskEntityOrThrow(Long id) {
       return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task by id" + id + " not found"));
    }

    private void checkAssignedUserId(Long userId){
        long count =  taskRepository.countByAssignedUserIdAndStatus(userId, TaskStatus.IN_PROGRESS);

        if (count > 4 ){
            throw new TaskCannotBeStartedException("Count assigned task more 4 ");
        }
    }
}
