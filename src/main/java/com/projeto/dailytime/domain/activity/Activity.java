package com.projeto.dailytime.domain.activity;

import com.projeto.dailytime.domain.task.Task;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "activity")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Activity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

}
