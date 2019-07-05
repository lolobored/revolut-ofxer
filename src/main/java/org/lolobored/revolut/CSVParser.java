package org.lolobored.revolut;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CSVParser {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMMMMMMMMMMM dd");
	private static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

	public static void main(String[] args) throws Exception {
		CSVParser parser = new CSVParser();

		parser.transformRevolutCSVToOFX(args[0]);
	}

	public void transformRevolutCSVToOFX(String csvPath) throws Exception {

		String thisYear = yearFormat.format(new Date());
		String parentFolder = Paths.get(csvPath).getParent().toString();
		List<RevolutCSVLine> csvLines = beanBuilderExample(Paths.get(csvPath), RevolutCSVLine.class);
		String csvText = new String(Files.readAllBytes(Paths.get(csvPath)));
		String header = StringUtils.substringBefore(csvText, "\n");
		String currency = StringUtils.substringBetween(header, "Paid Out (", ")");
		Statement statement = new Statement();
		statement.setCurrency(currency);
		for (RevolutCSVLine csvLine : csvLines) {
			Transaction tx = new Transaction();
			if (StringUtils.countMatches(csvLine.getDate(), ' ') == 1) {
				tx.setDate(dateFormat.parse(thisYear + " " + csvLine.getDate()));
			} else {
				tx.setDate(dateFormat.parse(csvLine.getDate()));
			}

			if (StringUtils.isNotEmpty(csvLine.getAmountIn())) {
				tx.setAmount(new BigDecimal(csvLine.getAmountIn().replace(",", "")));

			} else {
				tx.setAmount(new BigDecimal("-" + csvLine.getAmountOut().replace(",", "")));
			}
			tx.setReference(csvLine.getReference());
			statement.addTransaction(tx);
		}

String resultingFile= parentFolder+"/"+statement.getCurrency().toLowerCase()+".ofx";
		Path targetFile= Paths.get(resultingFile);
		Files.write( targetFile, StatementOFXer.writeOfx(statement).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		// echo the resulting file so that it can be used in automator
		System.out.println(resultingFile);
	}

	private List<RevolutCSVLine> beanBuilderExample(Path path, Class clazz) throws Exception {
		ColumnPositionMappingStrategy ms = new ColumnPositionMappingStrategy();
		ms.setType(clazz);

		Reader reader = Files.newBufferedReader(path);
		CsvToBean cb = new CsvToBeanBuilder(reader)
				.withSeparator(';')
				.withSkipLines(1)
				.withType(clazz)
				.withMappingStrategy(ms)
				.build();

		return cb.parse();
	}
}
