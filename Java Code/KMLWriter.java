package eot_nikola_christina;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class KMLWriter {
	

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//getting our lists with the values pulled from tweets.csv
		CSVPulling data = new CSVPulling();
		List<String> latitudes = data.getLats();
		List<String> longitudes = data.getLons();
		List<String> tweets = data.getTweets();
		List<String> falseDateTime = data.getDatetime();
		
//new		// create trueDateTime
		List<String> trueDateTime = new ArrayList<>();
	        
		for (String datetime : falseDateTime) {
	        	if (datetime.length() == 22) {
	        		
	        String truedatetime = datetime.substring(0, 10) + "T" + datetime.substring(11, 19) + "-04:00";
	        	trueDateTime.add(truedatetime);	
	           
	        	}
	        }
		
//new		// create random colors
        List<String> colourList = new ArrayList<>();
        Random random = new Random();
        for (String string : longitudes) {
            // Generate 6 random hexadecimal digits
            String hex = String.format("%06x", random.nextInt(0x1000000));
            String newColorLng = "ff" + hex;
            colourList.add(newColorLng);
        }
	        
	        
		//setting some path files
        String kmlFile = "tweetsViz.kml"; // Path to the output KML file
		String kmlFilePath = "\"C:\\Users\\ohits\\OneDrive - stud.sbg.ac.at\\swefinal_2\\tweetsViz.kml\"";
		String googleEarthFilePath = "\"C:\\Program Files\\Google\\Google Earth Pro\\client\\googleearth.exe\"";
		
		//here's where we start to write the kml
		try (FileWriter writer = new FileWriter(kmlFile)) {
        	// definition of xml elements 
            Namespace ns = Namespace.getNamespace("http://www.opengis.net/kml/2.2");
            Element kmlElement = new Element("kml", ns);
            Element documentElement = new Element("Document", ns);
            
            //everything relating to the WMS image overlay
            Element groundOverlayElement = new Element("GroundOverlay", ns);
            Element goColorElement = new Element("color", ns);
            Element goIcon = new Element("Icon", ns);
            Element goIconPath = new Element ("href", ns);
            Element goBounding = new Element ("LatLonBox", ns);
            Element bboxNorth = new Element("north", ns);
            Element bboxSouth = new Element("south", ns);
            Element bboxEast = new Element ("east", ns);
            Element bboxWest = new Element ("west", ns);
            
            
            String iconPath = "C:/Users/ohits/OneDrive - stud.sbg.ac.at/swefinal_2/MassGISCombined_output.png";
            
            goIconPath.setText(iconPath);
            goIcon.addContent(goIconPath);
            
            String color = "#B3515ba6";
            goColorElement.setText(color);
            
            String northCoord = "42.88";
            String southCoord = "41.23";
            String eastCoord = "-69.86";
            String westCoord = "-73.533";
            
            bboxNorth.setText(northCoord);
            bboxSouth.setText(southCoord);
            bboxEast.setText(eastCoord);
            bboxWest.setText(westCoord);
            
            goBounding.addContent(bboxNorth);
            goBounding.addContent(bboxSouth);
            goBounding.addContent(bboxEast);
            goBounding.addContent(bboxWest);
            
            groundOverlayElement.addContent(goColorElement);
            groundOverlayElement.addContent(goIcon);
            groundOverlayElement.addContent(goBounding);
            documentElement.addContent(groundOverlayElement);
            
            
            
            //Everything relating to the CSV data placemark creation
            for (int i = 0; i < latitudes.size(); i++) {
            	
            	// define xml elements
                Element placemarkElement = new Element("Placemark", ns);
                //introduce line string geometry for extruding visualization
                Element LineStringElement = new Element("LineString", ns);
                Element coordinatesElement = new Element("coordinates", ns);
                Element descriptionElement = new Element("description", ns);
                Element TimeStampElement = new Element("TimeStamp", ns);
                Element whenElement = new Element("when",ns);
                //introduce styleURL to call to the specific line style
                Element styleUrlElement = new Element("styleURL");
                Element styleElement = new Element("Style");
                Element lineStyleElement = new Element("LineStyle");
                Element polyStyleElement = new Element("PolyStyle");
                Element altitudeModeElement = new Element("altitudeMode"); 
                Element extrudeElement = new Element("extrude");
                // define coordinates, description value and value inside <when> </when> tag
                String coordinates = longitudes.get(i) + "," + latitudes.get(i) + ",0 " + longitudes.get(i) + "," + latitudes.get(i) + ",10000 ";
                String description = tweets.get(i);
               //set geometry styles 
                String colour = colourList.get(i);
                String when = trueDateTime.get(i);
                coordinatesElement.setText(coordinates);
                descriptionElement.setText(description);
                whenElement.setText(when);
                
                
                extrudeElement.addContent("1");
                altitudeModeElement.addContent("relativeToGround");
                LineStringElement.addContent(coordinatesElement);
                LineStringElement.addContent(extrudeElement);
                LineStringElement.addContent(altitudeModeElement);
                styleUrlElement.addContent("#" + colour);
                
                styleElement.setAttribute("id", colour);
                lineStyleElement.addContent(new Element("color").addContent(colour)); 
                lineStyleElement.addContent(new Element("width").addContent("5"));
                styleElement.addContent(lineStyleElement);
                
                
                TimeStampElement.addContent(whenElement);
                
                placemarkElement.addContent(TimeStampElement);
                placemarkElement.addContent(LineStringElement);
                placemarkElement.addContent(styleUrlElement);
                placemarkElement.addContent(descriptionElement);
                placemarkElement.addContent(styleElement);
                documentElement.addContent(placemarkElement);
               
            }
            
            // add documentElement to kmlElement
            kmlElement.addContent(documentElement);
            // add kmlElement to kmldocument
            Document kmlDocument = new Document(kmlElement);
            // write kmlDocument to a file
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(kmlDocument, writer);
            
            
          //launch Google Earth
			System.out.println("Launching Google Earth");
			
			//blank AaSCII encoding
			
			String[] parameters = new String[2];
			
			parameters[0] = googleEarthFilePath;
			parameters[1]= kmlFilePath;
			
			Runtime.getRuntime().exec(parameters);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		

	}

}