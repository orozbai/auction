package org.example.registrationpage.entities;

import lombok.*;

import java.util.Objects;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LotEntity {
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private Integer price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LotEntity lotEntity = (LotEntity) o;

        if (!Objects.equals(quantity, lotEntity.quantity)) return false;
        if (!Objects.equals(id, lotEntity.id)) return false;
        if (!Objects.equals(name, lotEntity.name)) return false;
        return Objects.equals(description, lotEntity.description);
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
