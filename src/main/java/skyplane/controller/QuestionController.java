package main.java.skyplane.controller;

import com.google.appengine.repackaged.com.google.gson.stream.JsonWriter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import main.java.skyplane.PMF;
import main.java.skyplane.TraderUtils;
import main.java.skyplane.model.Question;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by main.java.skyplane on 26.04.17.
 */
@RestController
@RequestMapping("/api/message")
public class QuestionController extends BaseController {

    private static final String DEFAULT_QUERY = "select from " + Question.class.getName();

    @RequestMapping(value = "/saveMessage", method = RequestMethod.GET)
    public void saveMessage(@RequestParam("question") String questionStr, HttpServletResponse response) throws UnsupportedEncodingException {
        Question question = new Question();
        question.setQuestion(questionStr);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        question.setPrincipal(user.getUsername());
        PersistenceManager pm = PMF.get().getPersistenceManager();
        pm.makePersistent(question);
        printSuccessStatus(response);
    }

    @RequestMapping(value = "/getMessages", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    public void getMessages(HttpServletResponse response) throws UnsupportedEncodingException {
        List<Question> questions = queryJDO(DEFAULT_QUERY);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isAdmin = false;
        for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
            }
        }

        try {
            OutputStreamWriter os = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            JsonWriter jsonWriter = new JsonWriter(os);
            jsonWriter.beginObject();
            jsonWriter.name("data");
            jsonWriter.beginArray();
            for (Question question : questions)
                if (isAdmin || (user.getUsername().equals(question.getPrincipal()))) {
                    jsonWriter.beginObject();
                    jsonWriter.name("id").value(question.getId());
                    jsonWriter.name("user").value(question.getPrincipal());
                    jsonWriter.name("question").value(question.getQuestion());
                    jsonWriter.name("answer").value(
                            (question.getAnswer() == null ? "" : question.getAnswer()) +
                                    (isAdmin ? ("<button class\"btn-primary\" " +
                                            "onclick=\"editAnswer('" + question.getAnswer() + "'," + question.getId() + ")\">edit</button>") : ""));
                    jsonWriter.name("date_display").value(TraderUtils.DATE_FORMAT_WITH_MINUTES.format(question.getCreated()));
                    jsonWriter.name("date_sorted").value(question.getCreated().getTime() + "");
                    jsonWriter.name("deleted").value((isAdmin ? ("<button class\"btn-danger\" " +
                            "onclick=\"deleteQuestion(" + question.getId() + ")\">x</button>") : ""));
                    jsonWriter.endObject();
                }

            jsonWriter.endArray();
            jsonWriter.endObject();
            jsonWriter.close();
        } catch (IOException ioe) {

        }
    }

    @RequestMapping(value = "/updateAnswer", method = RequestMethod.GET)
    public void updateAnswer(@RequestParam("id") Long id, @RequestParam("answer") String answer, HttpServletResponse response) throws UnsupportedEncodingException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        pm.currentTransaction().begin();
        Question question = pm.getObjectById(Question.class, id);
        question.setAnswer(answer);
        question.setUpdateDate(new Date());
        pm.currentTransaction().commit();
        printSuccessStatus(response);
    }


    @RequestMapping(value = "/deleteMessage", method = RequestMethod.GET)
    public void deleteMessage(@RequestParam("id") Long id, HttpServletResponse response) throws UnsupportedEncodingException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        pm.deletePersistent(pm.getObjectById(Question.class, id));

        printSuccessStatus(response);
    }


    private List<Question> queryJDO(String query) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            List<Question> questions = (List<Question>) pm.newQuery(query).execute();
            // Force all results to be pulled back before we close the entity manager.
            // We could have also called pm.detachCopyAll()
            questions.size();
            return questions;
        } finally {
            pm.close();
        }
    }


}
