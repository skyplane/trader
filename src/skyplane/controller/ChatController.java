package skyplane.controller;

import com.google.appengine.repackaged.com.google.gson.stream.JsonWriter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import skyplane.PMF;
import skyplane.TraderUtils;
import skyplane.model.FinancialSignal;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by skyplane on 28.04.17.
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController extends BaseController {

    ConcurrentLinkedDeque<String> chatQueue = new ConcurrentLinkedDeque<String>();

    @RequestMapping(value = "/saveMessage", method = RequestMethod.GET)
    public void saveSignal(@RequestParam("text") String text, HttpServletResponse response) throws UnsupportedEncodingException {
        if (chatQueue.size() > 10)
            chatQueue.removeLast();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        chatQueue.addFirst(user.getUsername()+": "+text);
        printSuccessStatus(response);
    }

    @RequestMapping(value = "/getChat", method = RequestMethod.GET)
    public void getSignals(HttpServletResponse response) throws UnsupportedEncodingException {
        try {
            OutputStreamWriter os = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            JsonWriter jsonWriter = new JsonWriter(os);
            jsonWriter.beginObject();
            jsonWriter.name("data");
            jsonWriter.beginArray();
            for (String text : chatQueue) {
                jsonWriter.value(text);
            }
            jsonWriter.endArray();
            jsonWriter.endObject();
            jsonWriter.close();
        } catch (IOException ioe) {

        }
    }
}
