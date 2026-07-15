package fedoseev.tasks.system.models.task;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "task")
@JsonPropertyOrder({"id", "creatorId", "assignedUserId", "status", "createDateTime", "deadlineDate", "priority"})
public class TaskEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creatorId")
    private Long creatorId;

    @Column(name = "assignedUserId")
    private Long assignedUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Column(name = "createDateTime")
    private LocalDateTime createDateTime;


    @Column(name = "deadlineDate")
    private LocalDateTime deadlineDate;

    @Column(name = "doneDateTime")
    LocalDateTime doneDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private TaskPriority priority;




}

