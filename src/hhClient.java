import java.io.*;
import java.util.*;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;

class hhClient {
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
                //System.out.println("Code: " + httpsURLConnection.getResponseCode() + " massage: " + httpsURLConnection.getResponseMessage());
                return streamToString(httpsURLConnection.getInputStream()); // response
            }else{
                System.out.println("ERROR! Code: " + httpsURLConnection.getResponseCode());
                return null;
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

}

class hhClientMain {
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(new File("researchData.txt"));
            PrintWriter out = new PrintWriter("resultData.txt");
            PrintWriter json = new PrintWriter("resulJSON.json");

            hhClient hhClient = new hhClient();

            //Map<String, String> researchData = new HashMap<>();
            List<String> keys = new ArrayList<>();
            keys.add("text");
            keys.add("area");

            StringBuilder responseText = new StringBuilder("https://api.hh.ru/vacancies");

            for(int i = 0; in.hasNextLine(); i++) {
                if(i == 0) {
                    responseText.append("?");
                }else {
                    responseText.append("&");
                }
                String fileLine = in.nextLine();

                //researchData.put(keys.get(i), fileLine);

                responseText.append(keys.get(i));
                responseText.append("=");
                responseText.append(URLEncoder.encode(fileLine, "utf-8"));
            }

            String response = hhClient.getResponse(responseText.toString());

            if (response != null) {
                System.out.println(response);
                json.print(response);
            }
            in.close();
            out.close();
            json.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
    }
}
