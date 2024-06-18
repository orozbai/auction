package org.example.registrationpage.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(name = "customer", schema = "public", catalog = "postgres")
public class CustomerEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "lot_id", nullable = true)
    private Integer lotId;
    @Basic
    @Column(name = "user_id", nullable = true)
    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerEntity that = (CustomerEntity) o;

        if (id != that.id) return false;
        if (!Objects.equals(lotId, that.lotId)) return false;
        if (!Objects.equals(userId, that.userId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (lotId != null ? lotId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
