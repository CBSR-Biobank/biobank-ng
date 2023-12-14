package edu.ualberta.med.biobank.repositories;

import edu.ualberta.med.biobank.domain.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(
        value = """
                select
                  p.ID,
                  p.FULL_NAME,
                  p.LOGIN,
                  cu.PASSWORD,
                  p.ACTIVITY_STATUS_ID,
                  grp.ID GROUP_ID,
                  grp.DESCRIPTION GROUP_NAME,
                  m.ID MEMBERSHIP_ID,
                  m.EVERY_PERMISSION,
                  m.DOMAIN_ID,
                  d.ALL_CENTERS,
                  d.ALL_STUDIES,
                  dc.CENTER_ID,
                  ds.CENTER_ID STUDY_ID,
                  r.ID ROLE_ID,
                  r.NAME ROLE_NAME,
                  rp.PERMISSION_ID ROLE_PERMISSION_ID,
                  mp.PERMISSION_ID MEMBERSHIP_PERMISSION_ID
                from principal p
                left join csm_user cu on cu.USER_ID  = p.CSM_USER_ID
                left join group_user gu on gu.USER_ID = p.ID
                left join principal grp on grp.ID = gu.GROUP_ID
                left join membership m on m.PRINCIPAL_ID = p.ID or m.PRINCIPAL_ID = gu.GROUP_ID
                left join `domain` d on d.id=m.DOMAIN_ID
                left join domain_center dc on dc.DOMAIN_ID = d.ID
                left join domain_study ds on ds.DOMAIN_ID = d.id
                left join membership_permission mp on mp.id=m.ID
                left join membership_role mr on mr.MEMBERSHIP_ID = m.ID
                left join `role` r on r.ID = mr.ROLE_ID
                left join role_permission rp on rp.id=r.ID
                where p.login = :login
                """,
        nativeQuery = true
    )
    <T> Collection<T> findByLogin(String login, Class<T> type);

    @EntityGraph("user-with-groups-and-memberships")
    public List<User> findAll(Specification<User> spec);
}
