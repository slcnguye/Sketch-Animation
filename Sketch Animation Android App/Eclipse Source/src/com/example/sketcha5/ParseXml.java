package com.example.sketcha5;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.graphics.Point;

public class ParseXml {
	public static Model parseXmlToModel(File file) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document xmlFile = db.parse(file);
			xmlFile.getDocumentElement().normalize();
			
			int numberOfFrames = Integer.parseInt(xmlFile.getElementsByTagName("numberOfDrawings").item(0).getTextContent());
			Model model = new Model(numberOfFrames);
	
			NodeList drawingsList = xmlFile.getElementsByTagName("drawing");
			for (int i = 0; i < drawingsList.getLength(); ++i) {
				Drawing drawingObject = getDrawing(drawingsList, i);
				if (drawingObject != null) {
					model.addDrawing(drawingObject);
				}
			}
			return model;
			//return the model which now contains all necessary information to finish up
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Drawing getDrawing(NodeList drawingsList, int i) {
		Node drawing = drawingsList.item(i);
		if (drawing.getNodeType() == Node.ELEMENT_NODE) {
			Element drawingEle = (Element) drawing;
			int colorValue = (int) Float.parseFloat(drawingEle.getElementsByTagName("color").item(0).getTextContent());
			float boldness = Float.parseFloat(drawingEle.getElementsByTagName("stroke").item(0).getTextContent());
			
			ArrayList<Point> points = getPointsFromString(drawingEle.getElementsByTagName("points").item(0).getTextContent());
			Drawing drawingObject = new Drawing(colorValue, boldness);
			NodeList frames = drawingEle.getElementsByTagName("frame");
			ArrayList<DrawingFrame> drawingFrames = getDrawingFrames(frames);
			drawingObject.setFrame(drawingFrames);
			drawingObject.setPoints(points);
			return drawingObject;
		} else {
			return null;
		}

	}

	private static ArrayList<DrawingFrame> getDrawingFrames(NodeList frames) {
		ArrayList<DrawingFrame> drawingFrames = new ArrayList<DrawingFrame>();
		for (int j = 0; j < frames.getLength(); ++j) {
			if (frames.item(j).getNodeType() == Node.ELEMENT_NODE) {
				Element frame = (Element) frames.item(j);

				NodeList cordList = frame.getElementsByTagName("cordinates");
				String cordPair = ((Element) cordList.item(0)).getTextContent();
				if (cordPair == null) {
					return drawingFrames;
				}	
				
				String[] cord = cordPair.split(",");
				int x = (int) Float.parseFloat(cord[0]);
				int y = (int) Float.parseFloat(cord[1]);
				
				NodeList visibilityList = frame.getElementsByTagName("visible");
				String value = ((Element) visibilityList.item(0)).getChildNodes().item(0).getNodeValue();
				if (value.equals("true")) {
					drawingFrames.add(new DrawingFrame(true, new Point(x,y)));
				} else {
					drawingFrames.add(new DrawingFrame(false, new Point(x,y)));
				}
			}
		}
		return drawingFrames;
		
	}

	private static ArrayList<Point> getPointsFromString(String textContent) {
		ArrayList<Point> points = new ArrayList<Point>();
		Scanner scanner = new Scanner(textContent);
		while (scanner.hasNext()) {
			int x = (int) Float.parseFloat(scanner.next());
			int y = (int) Float.parseFloat(scanner.next());
			points.add(new Point(x,y));
		}
		return points;
	}
}