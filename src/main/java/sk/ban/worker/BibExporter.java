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
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by BAN on 2. 2. 2015.
 */
public class BibExporter {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(BibExporter.class);

	private static final String SEPARATOR = "<!--*1*-->";
	private static final String END_OF_LINE = "}, \r\n";

	private static String conference;
	private static String year;
	private static String publishedDate;
	private static String registrationDate;

	private String exportedFile ;

	public BibExporter(String conference, String date, String publishedDate, String registrationDate) {
		this.conference = conference;
		this.year = date;
	}

	/**
	 * All data will be exported to the bib file
	 * @param dir
	 * @param parsedDocuments
	 */
	public void exportToFile(File dir, List<Document> parsedDocuments) {

		exportedFile = dir.toString() + "\\" + year + "-" + conference + "-1-1.bib";
		File bibFile = new File(exportedFile);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(bibFile))) {

			StringBuilder builder = new StringBuilder();
			for(Document dto : parsedDocuments) {
				builder.append(exportToString(dto));
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
	public static String exportToString(Document dto) {

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
		builder.append(key).append(",\r\n");
		builder.append("author      = {");
		StringBuilder authors = new StringBuilder(contentDTO.getAuthors().stream().collect(Collectors.joining(";")));

		authors.append("},\r\n");
		builder.append(authors.toString());

		builder.append("title       = {").append(contentDTO.getTitle()).append(END_OF_LINE);
		builder.append("subtitle    = {").append((contentDTO.getSubtitle() == null) ? "" : contentDTO.getSubtitle()).append(END_OF_LINE);
		builder.append("email    = {@" + END_OF_LINE);
		builder.append("year        = {").append(year).append(END_OF_LINE);
		builder.append("journal     = {").append(conference).append(END_OF_LINE);
		builder.append("chapter     = {").append((navigationDto != null) ? navigationDto.getSection() : "").append(END_OF_LINE);
		builder.append("pages       = {").append((navigationDto != null) ? navigationDto.getStartPage() : orderNumber)
				.append("--").append((navigationDto != null) ? navigationDto.getLastPage() : orderNumber).append(END_OF_LINE);
		builder.append("file        = {").append(key).append(".pdf").append(END_OF_LINE);
		builder.append("visibility  = {private" + END_OF_LINE);
		builder.append("registered  = {").append(registrationDate).append(END_OF_LINE);
		builder.append("published   = {").append(publishedDate).append(END_OF_LINE);
		builder.append("abstract    = {").append((contentDTO.getAbstractText() != null) ? contentDTO.getAbstractText() : "").append(END_OF_LINE);

		String keywordsInString = contentDTO.getKeywords().stream().collect(Collectors.joining(SEPARATOR));

		builder.append("keywords     = {").append(keywordsInString).append(END_OF_LINE);

		int index = 1;
		StringBuilder referencesBuilder = new StringBuilder();
		for (String reference : contentDTO.getReferences()) {

			referencesBuilder.append(index).append(" ").append(reference).append(SEPARATOR);
			index++;
		}
		builder.append("references   ={").append(referencesBuilder.toString()).append(END_OF_LINE);

		builder.append("}\r\n\r\n");
		orderNumber++;

		log.debug("Data exported to String");
		return builder.toString();
	}

	public  Optional<String> getPathToExportedFile(){
		return Optional.ofNullable(exportedFile);
	}
}
