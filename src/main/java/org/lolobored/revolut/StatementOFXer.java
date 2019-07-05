package org.lolobored.revolut;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class StatementOFXer {

	private static SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMdd");

	public static String writeOfx(Statement statement){

		StringBuilder ofx= new StringBuilder();
		ofx.append(getHeader());
		for (Transaction transaction : statement.getTransactions()) {
			ofx.append("\n" +
					"<STMTTRN>\n" +
					"<TRNTYPE>");
			if (transaction.getAmount().compareTo(BigDecimal.ZERO) >=0){
				ofx.append("CREDIT").append("\n");
			}
			else{
				ofx.append("DEBIT").append("\n");
			}
			ofx.append("<DTPOSTED>").append(dateFormat.format(transaction.getDate())).append("\n");
			ofx.append("<TRNAMT>").append(transaction.getAmount()).append("\n");
			ofx.append("<NAME>").append(transaction.getReference()).append("\n");
			ofx.append("</STMTTRN>");
		}
ofx.append(getTrailer());
return ofx.toString();
	}

	private static String getTrailer() {
		StringBuilder trailer = new StringBuilder();
		trailer.append("\n" +
				"\n" +
				"\n" +
				"</BANKTRANLIST>\n" +
				"<LEDGERBAL><BALAMT>0<DTASOF>20160803\n" +
				"\n" +
				"</LEDGERBAL>\n" +
				"</CCSTMTRS>\n" +
				"</CCSTMTTRNRS>\n" +
				"</CREDITCARDMSGSRSV1>\n" +
				"</OFX>");
		return trailer.toString();
	}

	private static String getHeader() {

		StringBuilder header = new StringBuilder();
		header.append("OFXHEADER:100\n" +
				"VERSION:102\n" +
				"DATA:OFXSGML\n" +
				"SECURITY:NONE\n" +
				"ENCODING:UTF-8\n" +
				"CHARSET:1252\n" +
				"COMPRESSION:NONE\n" +
				"OLDFILEUID:NONE\n" +
				"NEWFILEUID:NONE\n" +
				"\n" +
				"<OFX><SIGNONMSGSRSV1><SONRS><STATUS><CODE>0\n" +
				"<SEVERITY>INFO\n" +
				"<DTSERVER>20160825\n" +
				"<LANGUAGE>ENG\n" +
				"</STATUS>\n" +
				"</SONRS>\n" +
				"</SIGNONMSGSRSV1>\n" +
				"<CREDITCARDMSGSRSV1><CCSTMTTRNRS><CCSTMTRS><CURDEF>BRL<BANKACCTFROM><ACCTID>57a1bac9-52da-400f-ad71-99dda40b09fa\n" +
				"<ACCTTYPE>CREDITCARD\n" +
				"</BANKACCTFROM>\n" +
				"\n" +
				"<BANKTRANLIST><DTSTART>20160703<DTEND>20160803");
		return header.toString();
	}
}
