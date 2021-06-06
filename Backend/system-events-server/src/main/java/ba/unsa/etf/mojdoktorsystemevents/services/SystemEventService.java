package ba.unsa.etf.mojdoktorsystemevents.services;

import ba.unsa.etf.mojdoktorsystemevents.models.SystemEvent;
import ba.unsa.etf.mojdoktorsystemevents.repositories.SystemEventRepository;
import ba.unsa.etf.mojdoktorsystemevents.requests.DodajSystemEventRequest;
import ba.unsa.etf.mojdoktorsystemevents.responses.Response;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SystemEventService {

    private final SystemEventRepository systemEventRepository;

    public Response dodajSystemEvent(DodajSystemEventRequest dodajSystemEventRequest) {
        SystemEvent systemEvent = new SystemEvent(
                dodajSystemEventRequest.getTimestampAkcije(),
                dodajSystemEventRequest.getNazivMikroservisa(),
                dodajSystemEventRequest.getResurs(),
                dodajSystemEventRequest.getTipAkcije(),
                dodajSystemEventRequest.getTipOdgovoraNaAkciju()
        );
        systemEventRepository.save(systemEvent);
        return new Response("Uspješno ste dodali system-event!", 200);
    }
}
