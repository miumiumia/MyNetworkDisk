package googledisk;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.sql.Driver;
import java.util.Arrays;
import java.util.HashMap;


public class GoogleDrive {

  String CLIENT_ID = "870137320525-0iim6trjvvqr6kna4j4chtmv9aau4q5a.apps.googleusercontent.com";
  String CLIENT_SECRET = "C3CHZI01uIb7Zstd86yF1Ozd";
  Drive service;
  HashMap<String, File> files;
  HashMap<String, Integer> length;
  String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
  public void uploadFile(String name,java.io.File file) throws IOException{
	  //Insert a file  
	    File body = new File();
	    body.setTitle("name");
	    name=name+".txt";
	    body.setDescription("A test");
	    FileContent mediaContent = new FileContent("text/plain", file);
	    File file1 = service.files().insert(body, mediaContent).execute();
	    files.put(name, file1);
	    length.put(name, (int) file.length());
  }
  public byte[] downloadFile(String name) {
	  name=name+".txt";
	  File file=files.get(name);
	    if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
	      try {
	        HttpResponse resp =
	            service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
	                .execute();
	      	InputStream inputStream=resp.getContent();
	      	@SuppressWarnings("resource")
			FileOutputStream fileOutputStream=new FileOutputStream(new java.io.File("download/"+name));
	      	byte[] b=new byte[length.get(name)];
	      	inputStream.read(b);
	      	return b;
	      } catch (IOException e) {
	        // An error occurred.
	        e.printStackTrace();
	      }
	    }
		return null;
}
  public void init() throws IOException {
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
    files=new HashMap<>();
    length=new HashMap<>();
    System.out.println(DriveScopes.DRIVE);
    String[] temp=new String[1];
    temp[0]=DriveScopes.DRIVE;
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(temp))
        .setAccessType("online")
        .setApprovalPrompt("auto").build();
    
    String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    System.out.println("Please open the following URL in your browser then type the authorization code:");
    System.out.println("  " + url);
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String code = br.readLine();
    
    GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
    GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
    
    //Create a new authorized API client
    service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
  }
}