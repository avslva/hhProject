import java.io.*;
import java.util.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

class hhClient {
    private static Logger log = Logger.getLogger(hhClient.class.getName());

    public String getResponse(String request) {
        try {
            final URL url = new URL(request);
            final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            //httpsURLConnection.setDoInput(true);
            //httpsURLConnection.setRequestProperty("Content-Type", "application/json");
            //httpsURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            //httpsURLConnection.setReadTimeout(CONNECTION_TIMEOUT);
            if (httpsURLConnection.getResponseCode() != 503 && httpsURLConnection.getResponseCode() != 505) {
                log.log(Level.INFO, "Code: " + httpsURLConnection.getResponseCode() + " massage: " + httpsURLConnection.getResponseMessage());
                return streamToString(httpsURLConnection.getInputStream()); // response
            }else{
                log.log(Level.WARNING,"ERROR! Code: " + httpsURLConnection.getResponseCode());
                return null;
            }
        } catch(Exception e) {
            log.log(Level.SEVERE, "Exception: ", e);
            return e.getMessage();
        }
    }

    private String streamToString(InputStream is) throws IOException {
        String string = "";
        if(is != null){
            StringBuilder content = new StringBuilder();
            String inputLine;
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                while((inputLine = reader.readLine())!= null){
                    content.append(inputLine);
                }
                reader.close();
            } finally{
                is.close();
            }
            string = content.toString();
        }
        return string;
    }

    public String buildIdRequestText(Scanner in) throws IOException{
        //Map<String, String> researchData = new HashMap<>();
        List<String> keys = new ArrayList<>();
        keys.add("text");
        keys.add("area");
        /*keys.add("per_page");
        keys.add("pages");*/

        StringBuilder requestText = new StringBuilder("https://api.hh.ru/vacancies");

        for(int i = 0; in.hasNextLine(); i++) {
            if(i == 0) {
                requestText.append("?");
            }else {
                requestText.append("&");
            }
            String fileLine = in.nextLine();

            //researchData.put(keys.get(i), fileLine);

            requestText.append(keys.get(i));
            requestText.append("=");
            requestText.append(URLEncoder.encode(fileLine, "utf-8"));
        }
        return  requestText.toString();
    }

}