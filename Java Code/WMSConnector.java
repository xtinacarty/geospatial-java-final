package eot_nikola_christina;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.response.GetMapResponse;
import org.xml.sax.SAXException;

public class WMSConnector {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		URL url = null;
		try {
		  url = new URL("http://giswebservices.massgis.state.ma.us/geoserver/wms?service=wms&version=1.3.0&request=GetCapabilities");
		} catch (MalformedURLException e) {
		  //will not happen
		}

		//instantiate wms object
		WebMapServer wms = null;
		try {
		//fills wms object with our desired webmapserver from the URL
		  wms = new WebMapServer(url);
		} catch (IOException e) {
		  //There was an error communicating with the server
		  //For example, the server is down
		} catch (ServiceException e) {
		  //The server returned a ServiceException (unusual in this case)
		} catch (SAXException e) {
		  //Unable to parse the response from the server
		  //For example, the capabilities it returned was not valid
		}
		
		WMSCapabilities capabilities = wms.getCapabilities();

		//gets the top most layer, which will contain all the others
		//Layer rootLayer = capabilities.getLayer();

		//gets all the layers in a flat list, in the order they appear in
		//the capabilities document (so the rootLayer is at index 0)
		// List layers = capabilities.getLayerList();
		
		// System.out.println(layers);
		
		
		org.geotools.ows.wms.request.GetMapRequest request = wms.createGetMapRequest();
		request.setDimensions("583", "420");
		request.setFormat("image/png");
		request.setTransparent(true);
		request.setSRS("CRS:84");
		request.setBBox("-73.533, 41.23, -69.898, 42.888");
		//request.setSRS("EPSG:26986"); //pulled from Layer metadata
		//request.setBBox("33861.264,777514.311,330846.093,959747.441"); //pulled from layer metadata
		//request.setBBox("33869.691, 777514.57, 330846.231, 959743.12"); //pulled from layer metadata
		
		request.addLayer("GISDATA.CENSUS2000TRACTS_POLY", "");
		request.addLayer("GISDATA.PARCEL_STATUS", "");
		
		

		try {
			//actually pulling the data
			GetMapResponse response = (GetMapResponse) wms.issueRequest(request);
			BufferedImage image = ImageIO.read(response.getInputStream()); // storing the image
	        File outputfile = new File("MassGISCombined_output.png");
	        ImageIO.write(image, "png", outputfile); // writing it to local drive
			
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
