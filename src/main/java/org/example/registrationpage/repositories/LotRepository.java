package org.example.registrationpage.repositories;

import org.example.registrationpage.dtos.LotRegisterDto;
import org.example.registrationpage.entities.LotEntity;

import java.util.List;

public interface LotRepository {
    LotEntity getLotById(Long id);

    void saveLot(LotRegisterDto lot);

    void deleteLot(Long id);

    List<LotEntity> getAllLots();

    void updateLotById(LotRegisterDto updatedLot);
}
