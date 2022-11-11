package org.xmltojson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.XML;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    static JSONArray jsontemp2 = new JSONArray();

    public static void main(String[] args) throws Exception  {
        // TODO Auto-generated method stub
        String[] EastName = {"덕천","구룡포 하정","고성 봉포","온양","영덕","나곡","진하","고리","강릉"};
        String[] WestName = {"보령 효자도","서산 지곡","태안 파도리","태안 대야도","서산 창리","해남 임하","백령도","신안 압해","목포","군산 신시도"};
        String[] SouthName = {"장흥 회진","여수 화태","완도 노화도","완도 금일","완도 청산","통영 사량","통영 영운","통영 비산도","여수 신월","거제 일운","완도 망남","완도 일정","완도 가교","완도 동백","완도 백도","통영 수월","통영 풍화","통영 학림","남해 미조","해남 화산","고흥 소록도","거제 가배","남해 강진","장흥 회진","완도 송곡","완도 신흥","완도 미라","완도 망남","완도 중도","완도 일정","완도 횡간","완도 가학","완도 고마","완도 군외","완도 가교","완도 동촌","완도 동백","완도 방축","완도 백도","통영 수월","통영 풍화","통영 학림","남해 미조","진도 신전","장흥 사촌","장흥 노력","장흥 내저","진도 모도","장흥 이진목","진도 회동","진도 금갑","진도 도목","해남 어란","해남 송지","해남 상마","해남 송호","해남 옥동","해남 황산","해남 학가","해남 화산","해남 북일","고흥 영남","고흥 연소","강진 사초","고흥 신촌","고흥 소록도","고흥 남열","강진 마량","고흥 지죽","고흥 익금","고흥 화도","고흥 금산","거제 가배","고흥 동촌","보성 율포","보성 해평","보성 동율","남해 강진"};


        getApi("001","East",EastName,3);
        getApi("002","West",WestName,3);
        getApi("003","South",SouthName,3);

        JSONObject json = new JSONObject();
        json.put("data", jsontemp2);

        LocalDateTime Today = LocalDateTime.now();
        String TodayYMD = ( Integer.toString(Today.getYear()) +"-"+ Integer.toString(Today.getMonthValue()) +"-"+ Integer.toString(Today.getDayOfMonth()));
        String TodayY = Integer.toString(Today.getYear());
        String TodayM = Integer.toString(Today.getMonthValue());


        String apiData_DirectoryString ="E:/수과원API데이터";

        File createYearDir = new File(apiData_DirectoryString+"/"+TodayY);
        File createMonthDir = new File(apiData_DirectoryString+"/"+TodayY+"/"+TodayM);
        if(!createYearDir.exists()) {
            try {
                createYearDir.mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        if(createYearDir.exists()) {
            if(!createMonthDir.exists()) {
                try {
                    createMonthDir.mkdir();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            if(createMonthDir.exists()) {
                String ttt = createMonthDir+"/"+TodayYMD+"ApiData.json";

                FileWriter file = new FileWriter(ttt);
                file.write(json.toJSONString());
                file.flush();
                file.close();
            }
        }


		/*
		String ttt = "E:/수과원API데이터/"+TodayYMD+"ApiData.json";

		FileWriter file = new FileWriter(ttt);
		file.write(json.toJSONString());
		file.flush();
		file.close();
		*/

    }
    public static void getApi (String grunam, String gru,String[] type,int ex) {
        try {
            LocalDateTime EEDate = LocalDateTime.now();
            String EEDateSet = EEDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            LocalDateTime SSDate = EEDate.minusDays(1);
            String SSDateSet = SSDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));


            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1520635/OceanMensurationService/getOceanMesurationListrisa"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "키"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("STA_CDE","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /**/
            urlBuilder.append("&" + URLEncoder.encode("GRU_NAM","UTF-8") + "=" + URLEncoder.encode(grunam, "UTF-8")); /*동해,서해,남해*/
            urlBuilder.append("&" + URLEncoder.encode("SDATE","UTF-8") + "=" + URLEncoder.encode(SSDateSet, "UTF-8")); /*검색시작날짜*/
            urlBuilder.append("&" + URLEncoder.encode("EDATE","UTF-8") + "=" + URLEncoder.encode(EEDateSet, "UTF-8")); /*검색마지막날짜*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("4000", "UTF-8")); /*가져올갯수*/
            URL url = new URL(urlBuilder.toString());
            System.out.println(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            org.json.JSONObject xmlJSONObj = XML.toJSONObject(sb.toString());
            String a = xmlJSONObj.toString();


            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String,Object> map = new HashMap<>();
            map = objectMapper.readValue(a,new com.fasterxml.jackson.core.type.TypeReference<HashMap<String,Object>>() {
            });
            HashMap<String,Object> dataResponse = (HashMap<String,Object>) map.get("response");
            HashMap<String,Object> body = (HashMap<String,Object>) dataResponse.get("body");
            HashMap<String,Object> items = null;
            List<Map<String,Object>> itemList = null;
            items = (HashMap<String,Object>) body.get("items");
            itemList = (List<Map<String,Object>>) items.get("item");

            JSONArray jsontemp = new JSONArray();
            jsontemp.add(itemList);


            JSONObject json = new JSONObject();
            json.put("data", jsontemp);


            String[] name = new String[type.length];
            for (int i=0; i < type.length; i++) {
                name[i] = type[i];
            }
            int x = 0;


            for(int j=0; j<name.length; j++) {
                int check = 0;
                HashMap<String,Object> daa = new HashMap<>();
                for(int i=0; i<itemList.size(); i++) {
                    String now3 = itemList.get(i).get("obsDtm").toString();

                    LocalDateTime now = LocalDateTime.now();
                    String now2 = DateTimeFormatter.ofPattern("yyyy-MM-dd"+" 00:00:00").format(now);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date SDate = formatter.parse(now2);
                    Date EDate = formatter.parse(now3);

                    if(EDate.compareTo(SDate) >= 0) {
                        String date = itemList.get(i).get("obsDtm").toString();
                        String[] HourMinute = date.split("\\s");
                        date = HourMinute[1];
                        date = date.replace(":", "");
                        if(itemList.get(i).get("staNamKor").equals(name[j])) {
                            if(grunam.equals("001")) {
                                daa.put("name", name[j]+"(동해)");
                            }
                            else if(grunam.equals("002")) {
                                daa.put("name", name[j]+"(서해)");
                            }
                            else {
                                daa.put("name", name[j]+"(남해)");
                            }

                            daa.put("salt"+date, itemList.get(i).get("cdt_1"));
                            daa.put("dox"+date, itemList.get(i).get("dox_1"));
                            daa.put("temp"+date, itemList.get(i).get("wtrTmp_1"));
                            x++;
                        }
                    }
                    else {
                        check = 1;
                        break;
                    }
                }
                x=0;
                if(check != 1)
                    jsontemp2.add(daa);
                check = 0;
            }
            if(grunam.equals("001")) {
                System.out.println("---동해 데이터 추출성공---");
            }
            else if(grunam.equals("002")) {
                System.out.println("---서해 데이터 추출성공---");
            }
            else {
                System.out.println("---남해 데이터 추출성공---");
            }
        } catch (Exception e) {
            if(grunam.equals("001")) {
                System.out.println("---동해 데이터 추출실패---");
            }
            else if(grunam.equals("002")) {
                System.out.println("---서해 데이터 추출실패---");
            }
            else {
                System.out.println("---남해 데이터 추출실패---");
            }
            System.out.println("----재시도----");
            if(ex == 0) {
                System.out.println("데이터추출이 실패했습니다.");
                return;
            }
            else {
                getApi(grunam,gru,type,ex-1);
            }
        }
    }
}
