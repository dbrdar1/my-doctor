package ba.unsa.etf.doktordetalji.repositories;

import ba.unsa.etf.doktordetalji.models.Doktor;
import ba.unsa.etf.doktordetalji.requests.FilterRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class DoktorFilterRepositoryImpl implements DoktorFilterRepository {

    private final EntityManager em;

    @Override
    public List<Doktor> findByFilter(FilterRequest filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Doktor> cq = cb.createQuery(Doktor.class);

        Root<Doktor> root = cq.from(Doktor.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getIme() != null && !filter.getIme().isEmpty()) {
            String pattern = String.format("%%%s%%", filter.getIme());
            predicates.add(cb.like(root.get("ime"), pattern));
        }

        if (filter.getPrezime() != null && !filter.getPrezime().isEmpty()) {
            String pattern = String.format("%%%s%%", filter.getPrezime());
            predicates.add(cb.like(root.get("prezime"), pattern));
        }

        if (filter.getTitula() != null && !filter.getTitula().isEmpty()) {
            String pattern = String.format("%%%s%%", filter.getTitula());
            predicates.add(cb.like(root.get("titula"), pattern));
        }

        if (filter.getOcjena() != null && filter.getOcjena().compareTo(0) >= 0) {
            predicates.add(cb.lessThanOrEqualTo(root.get("ocjena"), filter.getOcjena()));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq).getResultList();
    }
}
