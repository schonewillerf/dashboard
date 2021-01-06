package hu.adsd.dashboard.messenger;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class MessengerController {
    private final MessageService messageService;

    public MessengerController(MessageService messageService) {
        this.messageService = messageService;
    }

    // update emmitter
    @CrossOrigin
    @RequestMapping(value = "/update", consumes = MediaType.ALL_VALUE)
    public SseEmitter updateEmitter()
    {
        SseEmitter sseEmitter=new SseEmitter(Long.MAX_VALUE);

        try {
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (Exception e) {
            sseEmitter.completeWithError(e);
        }

        sseEmitter.onCompletion(()->messageService.listEmitters.remove(sseEmitter));
        messageService.listEmitters.add(sseEmitter);

        return sseEmitter;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void updateEmittersAfterStartup() {
        updateEmitter();
    }
}
