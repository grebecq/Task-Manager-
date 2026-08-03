package fedoseev.tasks.system.task;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "assigned_user_id")
    private Long assignedUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Column(name = "create_date_time")
    private LocalDateTime createDateTime;


    @Column(name = "deadline_date")
    private LocalDateTime deadlineDate;

    @Column(name = "done_date_time")
    private LocalDateTime doneDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private TaskPriority priority;

}

