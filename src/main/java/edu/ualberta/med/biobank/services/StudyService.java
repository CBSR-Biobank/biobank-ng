package edu.ualberta.med.biobank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.dtos.StudyDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.repositories.StudyRepository;
import io.jbock.util.Either;

@Service
public class StudyService {
    @Autowired
    StudyRepository studyRepository;

    public void save(Study study) {
        studyRepository.save(study);
    }

    public Either<AppError, Study> getByStudyId(Integer id) {
        return studyRepository.findById(id)
            .map(Either::<AppError, Study>right)
            .orElseGet(() -> Either.left(new EntityNotFound("not found")));
    }

    public Page<StudyDTO> studyPagination(Integer pageNumber, Integer pageSize, String sort) {
        Pageable pageable = null;
        if (sort != null) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sort);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        Page<Study> data = studyRepository.findAll(pageable);
        return data.map(s -> new StudyDTO(s.getId(), s.getName(), s.getNameShort(), s.getActivityStatus().getName()));
    }
}
