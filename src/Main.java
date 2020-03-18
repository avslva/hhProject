import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger log = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        try (Scanner in = new Scanner(new File("researchData.txt"));
             PrintWriter out = new PrintWriter("resultData.txt") ){

            hhClient hhClient = new hhClient();

            String idRequest = hhClient.buildIdRequestText(in);

            String idResponse = hhClient.getResponse(idRequest);

            if (idResponse != null) {
                log.log(Level.INFO, "Response: " + idResponse);

                JsonParser parser = new JsonParser();
                ArrayList<String> vacanciesId = parser.getVacanciesId(idResponse);
                log.log(Level.INFO, "VacanciesId: " + vacanciesId);

                String vacancyResponseText = "https://api.hh.ru/vacancies/";

                ArrayList<String> skills = new ArrayList<>();

                for(String vacancyId : vacanciesId){
                    String vacancyResponse = hhClient.getResponse(vacancyResponseText+vacancyId);
                    skills.addAll(parser.getSkills(vacancyResponse));
                }
                log.log(Level.INFO, "Skills: " + skills);

                Map<String, Integer> skillCountMap = new HashMap<>();

                for(String skill : skills){
                    Integer newCount = 1;
                    if(skillCountMap.containsKey(skill)){
                        newCount = skillCountMap.get(skill) + 1;
                        skillCountMap.remove(skill);
                    }
                    skillCountMap.put(skill, newCount);
                }

                List list = new ArrayList<>(skillCountMap.entrySet());
                Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                        return b.getValue() - a.getValue();
                    }
                });

                for(Object skill : list){
                    out.println(skill);
                }
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Exception: ", e);
        }
    }
}
