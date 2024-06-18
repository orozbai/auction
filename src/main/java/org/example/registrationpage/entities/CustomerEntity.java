package org.example.registrationpage.entities;


import lombok.Data;

import java.util.Objects;

@Data
public class CustomerEntity {
    private int id;
    private Integer lotId;
    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerEntity that = (CustomerEntity) o;

        if (id != that.id) return false;
        if (!Objects.equals(lotId, that.lotId)) return false;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (lotId != null ? lotId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
