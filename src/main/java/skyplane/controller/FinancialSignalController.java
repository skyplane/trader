package main.java.skyplane.controller;

import com.google.appengine.repackaged.com.google.gson.stream.JsonWriter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import main.java.skyplane.PMF;
import main.java.skyplane.TraderUtils;
import main.java.skyplane.model.FinancialSignal;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by main.java.skyplane on 26.04.17.
 */
@RestController
@RequestMapping("/api/signal")
public class FinancialSignalController extends BaseController {


    private static final String DEFAULT_QUERY = "select from " + FinancialSignal.class.getName();

    @RequestMapping(value = "/saveSignal", method = RequestMethod.GET)
    public void saveSignal(@RequestParam("text") String text, HttpServletResponse response) throws UnsupportedEncodingException {
        FinancialSignal financialSignal = new FinancialSignal();
        financialSignal.setText(text);
        PersistenceManager pm = PMF.get().getPersistenceManager();
        pm.makePersistent(financialSignal);
        printSuccessStatus(response);
    }

    @RequestMapping(value = "/getSignals", method = RequestMethod.GET)
    public void getSignals(HttpServletResponse response) throws UnsupportedEncodingException {
        List<FinancialSignal> financialSignals = queryJDO(DEFAULT_QUERY);
        try {

            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            boolean isAdmin = false;
            for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
                if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                    isAdmin = true;
                }
            }

            OutputStreamWriter os = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            JsonWriter jsonWriter = new JsonWriter(os);
            jsonWriter.beginObject();
            jsonWriter.name("data");
            jsonWriter.beginArray();
            for (FinancialSignal financialSignal : financialSignals) {
                jsonWriter.beginObject();
                jsonWriter.name("id").value(financialSignal.getId());
                jsonWriter.name("text").value(financialSignal.getText());
                jsonWriter.name("date_display").value(TraderUtils.DATE_FORMAT_WITH_MINUTES.format(financialSignal.getCreated()));
                jsonWriter.name("date_sorted").value(financialSignal.getCreated().getTime()+"");
                jsonWriter.name("deleted").value((isAdmin?("<button class\"btn-danger\" " +
                        "onclick=\"deleteSignal(" + financialSignal.getId() + ")\">x</button>"):""));
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.endObject();
            jsonWriter.close();
        } catch (IOException ioe) {

        }
    }

    @RequestMapping(value = "/deleteSignal", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteSignal(@RequestParam("id") Long id, HttpServletResponse response) throws UnsupportedEncodingException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        pm.deletePersistent(pm.getObjectById(FinancialSignal.class, id));
        printSuccessStatus(response);
    }


    private List<FinancialSignal> queryJDO(String query) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            List<FinancialSignal> questions = (List<FinancialSignal>) pm.newQuery(query).execute();
            // Force all results to be pulled back before we close the entity manager.
            // We could have also called pm.detachCopyAll()
            questions.size();
            return questions;
        } finally {
            pm.close();
        }
    }

}
