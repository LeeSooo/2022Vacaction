package imageUpload;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

public class test {

	public static void main(String[] args) {
		String restApiKey = "";  // 개인 rest-api 키 입력

		try {
			String text = URLEncoder.encode("펭수", "UTF-8");
			String postParams = "src_lang=kr&target_lang=en&query=" + text;  // 파라미터
			String apiURL = "https://dapi.kakao.com/v2/search/image?" + postParams;
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			String userCredentials = restApiKey;
			String basicAuth = "KakaoAK " + userCredentials;
			con.setRequestProperty("Authorization", basicAuth);
			
			// 이건 필요 유무 몰라서 빼놈
			//con.setRequestMethod("GET");
			//con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//con.setRequestProperty("charset", "utf-8");
			//con.setUseCaches(false);
			//con.setDoInput(true);
			//con.setDoOutput(true);
			int responseCode = con.getResponseCode();
			System.out.println("responseCode >> " + responseCode);
			BufferedReader br;
			if(responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			}
			else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine);
				
			}
			br.close();
			//System.out.println("응답결과>> " + res.toString());

			// 가장 큰 JSONObject를 가져옵니다.
		    JSONObject jObject = new JSONObject(res.toString());
		    // 배열을 가져옵니다.
		    JSONArray jArray = jObject.getJSONArray("documents");

		    String savePath = "C:\\이미지테스트"; // 이미지 저장 파일
		    String fileFormat = "jpg";
			
		    // 배열의 모든 아이템을 출력합니다.
		    for (int i = 0; i < jArray.length(); i++) {
		        JSONObject obj = jArray.getJSONObject(i);
		        String imgURL = obj.getString("image_url");
		        
		        String saveFileName = "test" + i + ".jpg";
		        
		        File saveFile = new File(savePath + saveFileName);
		        
		        saveImage(imgURL, saveFile, fileFormat);
		        System.out.println("image_url(" + i + "): " + imgURL);
		        System.out.println();
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("--확인용-- T_Test.java에서 오류 발생");
			System.out.println(e);
		}
		
	}
	
	public static void saveImage(String imageUrl, File saveFile, String fileFormat) {
		URL url = null;
		BufferedImage bi = null;
		
		try {
			url = new URL(imageUrl); // 다운로드 할 이미지 URL
			bi = ImageIO.read(url);
			ImageIO.write(bi, fileFormat, saveFile); // 저장할 파일 형식, 저장할 파일명
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}