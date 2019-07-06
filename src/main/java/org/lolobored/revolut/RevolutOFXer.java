package org.lolobored.revolut;

import org.lolobored.revolut.csv.CSVParser;

public class RevolutOFXer {

	public static void main(String[] args) throws Exception {
		CSVParser parser = new CSVParser();

		// the first args is the path to the csv file
		// it allows to automate
		String ofxFile = parser.transformRevolutCSVToOFX(args[0]);
		// echo the resulting file so that it can be used in mac os automator
		// or any other automation software
		System.out.println(ofxFile);
	}
}
