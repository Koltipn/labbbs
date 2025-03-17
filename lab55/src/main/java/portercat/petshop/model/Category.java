package portercat.petshop.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "name")
    private String name;

    public Category() {
    }

}
