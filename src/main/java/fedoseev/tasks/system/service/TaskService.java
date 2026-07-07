package fedoseev.tasks.system.service;

import fedoseev.tasks.system.models.CreatedTaskRequest;
import fedoseev.tasks.system.models.TaskEntity;
import fedoseev.tasks.system.models.TaskStatus;
import fedoseev.tasks.system.repositories.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService{

 @Autowired
 private TaskRepository taskRepository;

  private static final Logger log = LoggerFactory.getLogger(TaskService.class);


    public TaskEntity getTaskById(Long id) {

        if (id == null || id <= 0) {
            throw new NoSuchElementException("Task id must be positive");
        }
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
    }

  public List<TaskEntity> getAllTasks(){

        return taskRepository.findAll();
  }

    public TaskEntity createdTask(
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

        TaskEntity newTask = new TaskEntity();

        newTask.setCreatorId(request.creatorId());

        newTask.setAssignedUserId(request.assignedUserId());

        newTask.setStatus(TaskStatus.CREATED);

        newTask.setCreateDateTime(request.createDateTime());

        newTask.setDeadlineDate(request.deadlineDate());

        newTask.setPriority(request.priority());

        TaskEntity savedTask = taskRepository.save(newTask);
        log.info("Created task information id={} and CreatedTaskRequest={} ",savedTask.getId(),request);

        return savedTask;
    }

    public TaskEntity updatedTask(
            @PathVariable Long id,
            @RequestBody TaskEntity taskToUpdate) {

        log.info("Called method updatedTask id={} and taskToUpdate={}",id,taskToUpdate);


        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task id=" + id + " not found"));


        if (existingTask.getStatus() == TaskStatus.DONE
                && taskToUpdate.getStatus() != TaskStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Cannot update task id=" + id + " because it is already DONE");
        }

        existingTask.setCreatorId(taskToUpdate.getCreatorId());
        existingTask.setAssignedUserId(taskToUpdate.getAssignedUserId());
        existingTask.setStatus(taskToUpdate.getStatus());
        existingTask.setCreateDateTime(taskToUpdate.getCreateDateTime());
        existingTask.setDeadlineDate(taskToUpdate.getDeadlineDate());
        existingTask.setPriority(taskToUpdate.getPriority());

        var savedUpdated = taskRepository.save(existingTask);
        log.info("Updated task by id={} and Entity={}",id,taskToUpdate);
        return savedUpdated;
    }

    public void deletedById(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Task by id" + id + " not found"));
        taskRepository.deleteById(id);
    }

    public TaskEntity startTask(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Task by id" + id + " not found"));
        if (taskEntity.getAssignedUserId() == null){
            throw  new IllegalArgumentException("AssignedUserId must be not null");
        }
        Long count =  taskRepository.countByAssignedUserIdAndStatus(taskEntity.getAssignedUserId(), TaskStatus.IN_PROGRESS);

        if (count > 4 ){
            throw new IllegalArgumentException("Count assigned task more 4 ");
        }
        taskEntity.setStatus(TaskStatus.IN_PROGRESS);

        return taskRepository.save(taskEntity);
    }
}
