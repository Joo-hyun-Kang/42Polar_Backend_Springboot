package com._polar._polar_backend_spring.v1.cadets;

import com._polar._polar_backend_spring.domain.entity.Cadets;
import com._polar._polar_backend_spring.v1.cadets.dto.common.CreateCadetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CadetsService {

    private final CadetsRepository cadetsRepository;

    public boolean isCadet(String intraId) {
        return cadetsRepository.findByIntra(intraId) != null;
    }

    public Cadets findByIntraId(String intraId) {
        return cadetsRepository.findByIntra(intraId);
    }

    public Cadets createUser(CreateCadetDto cadetDto) {

        Cadets cadet = new Cadets(cadetDto.getIntraId(), cadetDto.getProfileImage(), cadetDto.isCommon(), cadetDto.getEmail());
        cadetsRepository.save(cadet);

        return cadet;
    }

    public void updateLogin(Cadets cadet, CreateCadetDto updateData) {
        cadet.setIntraId(updateData.getIntraId());
        cadet.setProfileImage(updateData.getProfileImage());
        cadet.setCommon(updateData.isCommon());
        cadet.setEmail(updateData.getEmail());
    }

    public boolean validateInfo(Cadets cadet) {
        return cadet.getName() != null;
    }
}
