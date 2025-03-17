package portercat.petshop.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.List;

import jakarta.persistence.*;
@Entity
@Data
@Table(name = "pet")
public class Pet
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Tag> tags;

    @Column(nullable = false, name = "status")
    private String status;

}

