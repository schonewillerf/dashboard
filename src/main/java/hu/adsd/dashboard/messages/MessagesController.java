package hu.adsd.dashboard.messages;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class MessagesController {
    private final MessagesService messagesService;

    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
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

        sseEmitter.onCompletion(()-> messagesService.listEmitters.remove(sseEmitter));
        messagesService.listEmitters.add(sseEmitter);

        return sseEmitter;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void updateEmittersAfterStartup() {
        updateEmitter();
    }
}
