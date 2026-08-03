package fedoseev.tasks.system.task;


import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskEntity toEntity(Task task) {
        TaskEntity entity = new TaskEntity();
        entity.setId(task.id());
        entity.setCreatorId(task.creatorId());
        entity.setAssignedUserId(task.assignedUserId());
        entity.setStatus(task.status());
        entity.setCreateDateTime(task.createDateTime());
        entity.setDeadlineDate(task.deadlineDate());
        entity.setDoneDateTime(task.doneDateTime());
        entity.setPriority(task.priority());
        return entity;
    }

    public Task toModel(TaskEntity entity) {
        return new Task(
                entity.getId(),
                entity.getCreatorId(),
                entity.getAssignedUserId(),
                entity.getStatus(),
                entity.getCreateDateTime(),
                entity.getDeadlineDate(),
                entity.getDoneDateTime(),
                entity.getPriority()
        );
    }
}