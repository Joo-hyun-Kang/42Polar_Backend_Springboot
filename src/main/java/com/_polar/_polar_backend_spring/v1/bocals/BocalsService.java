package com._polar._polar_backend_spring.v1.bocals;

import com._polar._polar_backend_spring.domain.entity.Bocals;
import com._polar._polar_backend_spring.domain.entity.Mentors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BocalsService {

    private final BocalsRepository bocalsRepository;

    public boolean isBocal(String intraId) {
        return bocalsRepository.findByIntra(intraId) != null;
    }

    public Bocals findByIntra(String intraId) {
        return bocalsRepository.findByIntra(intraId);
    }

    @Transactional
    public Bocals createUser(String intraId) {
        Bocals bocal = new Bocals(intraId);
        bocalsRepository.save(bocal);

        return bocal;
    }

    @Transactional
    public void updateLogin(Bocals bocal, String intraId) {
        bocal.setIntraId(intraId);
    }
}
