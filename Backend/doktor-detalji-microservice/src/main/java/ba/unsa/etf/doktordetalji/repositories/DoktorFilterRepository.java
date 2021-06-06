package ba.unsa.etf.doktordetalji.repositories;

import ba.unsa.etf.doktordetalji.models.Doktor;
import ba.unsa.etf.doktordetalji.requests.FilterRequest;

import java.util.List;

public interface DoktorFilterRepository {
    List<Doktor> findByFilter(FilterRequest filter);
}
