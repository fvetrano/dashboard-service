package it.tim.dashboard.cms;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CMSConfiguration {

	public static final String FONIA = "FONIA";
	public static final String SMS = "SMS";
	public static final String DATI = "DATI";
	
	private static CMSConfiguration instance = null;
	
	private HashMap<String, Promotion> promotionsMap = new HashMap<String, Promotion>();
	private HashMap<String, Profile> profilesMap = new HashMap<String, Profile>();
	
	
	private CMSConfiguration(String promotionsFileName, String profilesFileName) {
		loadPromotions(promotionsFileName);
		loadProfiles(profilesFileName);
	}
	
	
	public static CMSConfiguration getinstance(String promotionsFileName, String profilesFileName) {
		if(instance == null) {
			instance = new CMSConfiguration(promotionsFileName, profilesFileName);
		}
		return instance;
	}

	
	
	
	private String getJsonStringFromFile(String fileName) throws Exception{
		
		Stream<String> stream = null;
		try {

			log.debug("read file ["+fileName+"]");
			
			
			StringBuilder contentBuilder = new StringBuilder();
			stream = Files.lines( Paths.get(fileName), StandardCharsets.UTF_8); 

			stream.forEach(s -> contentBuilder.append(s));
	 
	        String jsonString = contentBuilder.toString();
			
	        log.debug("jsonString: " + jsonString);
			
	        return jsonString;
	        
	        
		} 
		catch (Exception ex) {
			//ex.printStackTrace();
			log.error("Unable to load Traffic description from file ["+fileName+"] " + ex);
			throw ex;
		} 
		finally {
			if (stream != null) {
				try {
					stream.close();
				} 
				catch (Exception e) {
					log.error("Error in closing input stream " + e);
				}
			}
		}

	}

	
	
	public void loadPromotions(String fileName){
		
		
		try {

			String jsonString = getJsonStringFromFile(fileName);
			
	        List<Promotion> offersList = new ObjectMapper().readValue(jsonString, new TypeReference<List<Promotion>>(){});
	        
	        for(int i=0; i<offersList.size(); i++) {
	        	promotionsMap.put(offersList.get(i).getId(), offersList.get(i));
	        }
	        
	        log.debug("Promotion Map = ["+promotionsMap+"]");
	        
		} 
		catch (Exception ex) {
			log.error("Unable to load Promotions from file ["+fileName+"] " + ex);
		} 
	}

	
	
	
	public void loadProfiles(String fileName){
		
		try {

			String jsonString = getJsonStringFromFile(fileName);
			
	        List<Profile> profileList = new ObjectMapper().readValue(jsonString, new TypeReference<List<Profile>>(){});
	        
	        for(int i=0; i<profileList.size(); i++) {
	        	Profile p = profileList.get(i);
	        	profilesMap.put(profileList.get(i).getId(), profileList.get(i));
	        }
	        
	        log.debug("Profile Map = ["+profilesMap+"]");
	        
		} 
		catch (Exception ex) {
			log.error("Unable to load Profiles from file ["+fileName+"] " + ex);
		} 
	}

	
	public Promotion getPromotionById(String promotionId) {
		return promotionsMap.get(promotionId);
	}

	
	public Profile getProfileById(String profileId) {
		return profilesMap.get(profileId);
	}
	
	

	public static void main(String[] args) {
		String promotionsFileName = "C:/Users/s228343/git/dashboard-service/src/main/resources/promotions.json";
		String profilesFileName = "C:/Users/s228343/git/dashboard-service/src/main/resources/profiles.json";
		
		CMSConfiguration conf = CMSConfiguration.getinstance(promotionsFileName, profilesFileName);
				
	}


}
