package hu.adsd.dashboard.messages;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessagesService {
    public List<SseEmitter> listEmitters=new CopyOnWriteArrayList<>();

    public void sendMessage(String message) {
        //dispatch sse emmiter
        for (SseEmitter emitter:listEmitters)
        {
            try {
                emitter.send(SseEmitter.event().name("updates").data(message));
            } catch (Exception e) {
                listEmitters.remove(emitter);
                emitter.completeWithError(e);
            }
        }
    }
}

