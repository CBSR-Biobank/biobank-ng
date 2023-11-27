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
import edu.ualberta.med.biobank.permission.patient.StudyReadPermission;
import edu.ualberta.med.biobank.repositories.StudyRepository;
import io.jbock.util.Either;
import jakarta.persistence.Tuple;

@Service
public class StudyService {
    @Autowired
    StudyRepository studyRepository;

    public void save(Study study) {
        studyRepository.save(study);
    }

    public Either<AppError, StudyDTO> getByStudyId(Integer id) {
        var found = studyRepository.findById(id, Tuple.class).stream().findFirst();

        if (found.isEmpty()) {
            return Either.left(new EntityNotFound("study"));
        }

        var study = StudyDTO.fromTuple(found.get());

        var permission = new StudyReadPermission(id);
        var allowed = permission.isAllowed();
        return allowed.map(a -> study);
    }

    public Page<StudyDTO> studyPagination(Integer pageNumber, Integer pageSize, String sort) {
        // FIXME: check user memberships here and return only studies they have access to
        Pageable pageable = null;
        if (sort != null) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sort);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        Page<Tuple> data = studyRepository.findAll(pageable, Tuple.class);
        return data.map(d -> StudyDTO.fromTuple(d));
    }
}
