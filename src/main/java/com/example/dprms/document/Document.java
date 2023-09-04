package com.example.dprms.document;

import com.example.dprms.project.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "documents")
public class Document {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String documentTitle;
        private String documentName;
        private Date uploadedAt;

        @PrePersist
        public void prePersist() {
                uploadedAt = new Date(); // Set default timestamp value
        }

        @ManyToOne
        @JoinColumn(name = "project_id")
        private Project project; // Project to which this document belongs


}
