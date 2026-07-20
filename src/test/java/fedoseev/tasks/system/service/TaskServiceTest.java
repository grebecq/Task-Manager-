package fedoseev.tasks.system.service;

import fedoseev.tasks.system.exceptions.InvalidTaskIdException;
import fedoseev.tasks.system.exceptions.TaskCannotBeStartedException;
import fedoseev.tasks.system.exceptions.TaskNotFoundException;
import fedoseev.tasks.system.models.task.Task;
import fedoseev.tasks.system.models.task.TaskEntity;
import fedoseev.tasks.system.models.task.TaskMapper;
import fedoseev.tasks.system.models.task.TaskStatus;
import fedoseev.tasks.system.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Mock
    TaskEntity taskEntity;

    @Test
    void getTaskById_shouldReturnTask_whenTaskExists() {
        Long taskId = 1L;

        TaskEntity entityFromDb = new TaskEntity();
        entityFromDb.setId(taskId);
        entityFromDb.setCreatorId(5L);
        entityFromDb.setStatus(TaskStatus.CREATED);

        Task expectedTask = new Task(taskId, 5L, null, TaskStatus.CREATED, null, null, null, null);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(entityFromDb));
        when(taskMapper.toModel(entityFromDb)).thenReturn(expectedTask);

        Task result = taskService.getTaskById(taskId);

        assertThat(result).isEqualTo(expectedTask);
    }

    @Test
    void getTaskById_shouldThrowTaskNotFoundException_whenTaskNotFound() {
 
        Long taskId = 1L;

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                taskService.getTaskById(taskId))
                .isInstanceOf(TaskNotFoundException.class);

    }

    @Test
    void getTaskById_shouldThrowNInvalidTaskIdException_whenTaskNotFound() {


        assertThatThrownBy(() ->
                taskService.getTaskById(null))

        .isInstanceOf(InvalidTaskIdException.class);
    }

    @Test
    void startTask_shouldThrowTaskCannotBeStartedException_WhenTaskMore4(){

        Long taskId = 1L;
        Long assignedUserId = 11L;

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TaskStatus.CREATED);
        taskEntity.setAssignedUserId(assignedUserId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.countByAssignedUserIdAndStatus(assignedUserId,TaskStatus.IN_PROGRESS)).thenReturn(5L);

        assertThatThrownBy(() ->
                taskService.startTask(taskId))
                .isInstanceOf(TaskCannotBeStartedException.class);
    }

    @Test
    void startTask_ShouldSetStatusToInProgress_WhenLimitNotExceeded(){

        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setAssignedUserId(5L);
        taskEntity.setStatus(TaskStatus.CREATED);
        Task expectedTask = new Task(taskId, null, 5L, TaskStatus.IN_PROGRESS, null, null, null, null);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.countByAssignedUserIdAndStatus(5L,TaskStatus.IN_PROGRESS))
                .thenReturn(4L);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toModel(taskEntity)).thenReturn(expectedTask);

        Task result = taskService.startTask(taskId);

        assertThat(result).isEqualTo(expectedTask);
    }
}

