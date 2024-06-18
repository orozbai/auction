package org.example.registrationpage.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lot", schema = "public", catalog = "postgres")
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name", nullable = true, length = 70)
    private String name;
    @Basic
    @Column(name = "description", nullable = true, length = 300)
    private String description;
    @Basic
    @Column(name = "quantity", nullable = true)
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LotEntity lotEntity = (LotEntity) o;

        if (quantity != lotEntity.quantity) return false;
        if (id != null ? !id.equals(lotEntity.id) : lotEntity.id != null) return false;
        if (name != null ? !name.equals(lotEntity.name) : lotEntity.name != null) return false;
        if (description != null ? !description.equals(lotEntity.description) : lotEntity.description != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + quantity;
        return result;
    }
}
