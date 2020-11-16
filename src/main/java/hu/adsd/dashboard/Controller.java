package hu.adsd.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    private Repository repository;

    @GetMapping("/indicator")
    public List<Indicator> getIndicators(){
        return repository.findAll();
    }

    @GetMapping("/indicator/{id}")
    public Indicator getIndicator(@PathVariable int id){
        return repository.findById(id).orElseThrow();
    }

    @PostMapping("/indicator")
    public List<Indicator> addIndicator(@RequestBody Indicator indicator){
        repository.save(indicator);
        return repository.findAll();
    }

    @PutMapping("/indicator")
    public List<Indicator> updateIndicator(@RequestBody Indicator indicator){
        repository.findById(indicator.getId()).orElseThrow();
        repository.save(indicator);
        return repository.findAll();
    }

    @CrossOrigin(origins ="*", allowedHeaders ="*")
    @DeleteMapping("/indicator/{id}")
    public List<Indicator> deleteIndicator(@PathVariable int id){
        repository.deleteById(id);
        return repository.findAll();
    }

}
