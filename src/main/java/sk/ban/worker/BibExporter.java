package sk.ban.worker;

import sk.ban.data.Document;
import sk.ban.data.DocumentContent;
import sk.ban.data.DocumentNavigation;
import org.slf4j.LoggerFactory;
import sk.ban.exception.ParserException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by USER on 2. 2. 2015.
 */
public class BibExporter {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(BibExporter.class);

	private static final String SEPARATOR = "<!--*1*-->";
	private static final String END_OF_LINE = "}, \r\n";

	private static String conference;
	private static String year;
	private static String publishedDate;
	private static String registrationDate;

	public BibExporter(String conference, String date, String publishedDate, String registrationDate) {
		this.conference = conference;
		this.year = date;
	}

	/**
	 * All data will be exported to the bib file
	 * @param dir
	 * @param parsedDocuments
	 */
	public void exporAllDataToFile(File dir, List<Document> parsedDocuments) {

		File bibFile = new File(dir.toString() + "\\" + year + "-" + conference + "-1-1.bib");

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(bibFile))) {

			StringBuilder builder = new StringBuilder();
			for(Document dto : parsedDocuments) {
				builder.append(exportDataToString(dto));
			}
			writer.write(builder.toString());
		} catch (IOException ex) {
			throw new ParserException("Export data to bib format problem: " + ex);
		}
		log.info("Data was successfully saved in bib file. " + bibFile);
	}

	/**
	 * Data from parsed file can be exported to the string
	 * @param dto
	 * @return
	 */
	public static String exportDataToString(Document dto) {

		if(dto == null){
			log.info("Export data to bib: dto is null ");
			return "";
		}
		StringBuilder builder = new StringBuilder();
		int orderNumber = 1;

		DocumentContent contentDTO = dto.getContentData();
		DocumentNavigation navigationDto = dto.getAdditionalData();

		builder.append("@inproceedings{");
		String key = contentDTO.getFileName().substring(contentDTO.getFileName().lastIndexOf("\\") + 1, contentDTO.getFileName().lastIndexOf("."));
		builder.append(key + ",\r\n");
		builder.append("author      = {");
		StringBuilder authors = new StringBuilder(contentDTO.getAuthors().stream().collect(Collectors.joining(";")));

		authors.append("},\r\n");
		builder.append(authors.toString());

		builder.append("title       = {" + contentDTO.getTitle() + END_OF_LINE);
		builder.append("subtitle    = {" + ((contentDTO.getSubtitle() == null) ? "" : contentDTO.getSubtitle()) + END_OF_LINE);
		builder.append("email    = {@" + END_OF_LINE);
		builder.append("year        = {" + year + END_OF_LINE);
		builder.append("journal     = {" + conference + END_OF_LINE);
		builder.append("chapter     = {" + ((navigationDto != null) ? navigationDto.getSection() : "") + END_OF_LINE);
		builder.append("pages       = {" + ((navigationDto != null) ? navigationDto.getStartPage() : orderNumber) +
				"--" + ((navigationDto != null) ? navigationDto.getLastPage() : orderNumber) + END_OF_LINE);
		builder.append("file        = {" + key + ".pdf" + END_OF_LINE);
		builder.append("visibility  = {private" + END_OF_LINE);
		builder.append("registered  = {" + registrationDate + END_OF_LINE);
		builder.append("published   = {" + publishedDate + END_OF_LINE);
		builder.append("abstract    = {" + ((contentDTO.getAbstractText() != null) ? contentDTO.getAbstractText() : "") + END_OF_LINE);

		String keywordsInString = contentDTO.getKeywords().stream().collect(Collectors.joining(SEPARATOR));

		builder.append("keywords     = {" + keywordsInString + END_OF_LINE);

		int index = 1;
		StringBuilder referencesBuilder = new StringBuilder();
		for (String reference : contentDTO.getReferences()) {

			referencesBuilder.append(index + " " + reference + SEPARATOR);
			index++;
		}
		builder.append("references   ={" + referencesBuilder.toString() + END_OF_LINE);

		builder.append("}\r\n\r\n");
		orderNumber++;

		log.debug("Data exported to String");
		return builder.toString();
	}
}
