import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class JsonParser {
    private static Logger log = Logger.getLogger(JsonParser.class.getName());

    public ArrayList<String> getVacanciesId(String responseVacancies){
        ArrayList<String> idList = new ArrayList<>();
        try {
            JSONObject receivedJson = new JSONObject(responseVacancies);
            JSONArray vacancies = receivedJson.getJSONArray("items");

            for (Object vacancy : vacancies) {
                JSONObject jsonVacancy = new JSONObject(vacancy.toString());
                idList.add(jsonVacancy.get("id").toString());
            }
        }catch(JSONException e){
            log.log(Level.SEVERE, "Exception: ", e);
        }
        return idList;
    }

    public ArrayList<String> getSkills(String responseVacancy){
        ArrayList<String> skillsList = new ArrayList<>();
        try {
            JSONObject vacancy = new JSONObject(responseVacancy);
            JSONArray skills = vacancy.getJSONArray("key_skills");

            for(Object skill : skills){
                JSONObject jsonSkill = new JSONObject(skill.toString());
                skillsList.add(jsonSkill.get("name").toString());
            }
        }catch(JSONException e){
            log.log(Level.SEVERE, "Exception: ", e);
        }
        return skillsList;
    }
}
