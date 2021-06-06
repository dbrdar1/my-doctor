package ba.unsa.etf.mojdoktorsystemevents.controllers;

import ba.unsa.etf.grpc.ActionRequest;
import ba.unsa.etf.mojdoktorsystemevents.models.SystemEvent;
import ba.unsa.etf.mojdoktorsystemevents.repositories.SystemEventRepository;
import ba.unsa.etf.mojdoktorsystemevents.requests.DodajSystemEventRequest;
import ba.unsa.etf.mojdoktorsystemevents.responses.Response;
import ba.unsa.etf.mojdoktorsystemevents.services.SystemEventService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class SystemEventController {

    private final SystemEventRepository systemEventRepository;

    private final SystemEventService systemEventService;

    @GetMapping("/")
    public @ResponseBody String inicijalizacija(){
        return inicijalizirajBazu();
    }

    public String inicijalizirajBazu() {

        systemEventRepository.deleteAllInBatch();
        systemEventRepository.flush();

        SystemEvent systemEvent = new SystemEvent(
                "nesto",
                "nesto",
                "nesto",
                ActionRequest.TipAkcije.GET,
                ActionRequest.TipOdgovoraNaAkciju.USPJEH
        );

        systemEventRepository.save(systemEvent);

        return "Inicijalizacija baze završena!";
    }

    @GetMapping("/system-events")
    List<SystemEvent> all() {
        return systemEventRepository.findAll();
    }

    @GetMapping("/system-events/{id}")
    SystemEvent one(@PathVariable Long id) throws Exception {
        return systemEventRepository.findById(id)
                .orElseThrow(() -> new Exception("Korisnik nije pronađen"));
    }

    @PostMapping("/system-events")
    public ResponseEntity<Response> dodajSystemEvent(@RequestBody DodajSystemEventRequest dodajSystemEventRequest){
        Response response = systemEventService.dodajSystemEvent(dodajSystemEventRequest);
        return ResponseEntity.ok(response);
    }
}
