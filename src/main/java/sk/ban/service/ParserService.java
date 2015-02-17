package sk.ban.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.ban.enums.FileExtension;
import sk.ban.exception.ParserException;
import sk.ban.data.Document;
import sk.ban.parser.Parserable;
import sk.ban.parser.docx.DOCXParser;
import sk.ban.parser.pdf.MyPDFParser;
import sk.ban.util.FileUtil;
import sk.ban.worker.DataCombiner;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by USER on 21. 1. 2015.
 */
public class ParserService {

	private static final Logger log = LoggerFactory.getLogger(ParserService.class);

	@Inject
	private DOCXParser docxParser;

	@Inject
	private MyPDFParser pdfParser;

	private ExecutorService executor = Executors.newCachedThreadPool();

	private Map<String, List<Document>> parsedData;

	private boolean running = true;

	private static final String NONE_VALID_TITLE = "##################### ";

	/**
	 * @param fileOrDir
	 */
	public void parseData(File fileOrDir) {

		if (fileOrDir == null || !fileOrDir.exists()) {
			log.info("File for parsing was not found");
			return;
		}

		parsedData = new ConcurrentHashMap<>();
		setRunning(true);

		log.info("Start parsing");

		if (fileOrDir.isDirectory()) {
			parseDataInDir(fileOrDir);
		} else {
			parseSingleFile(fileOrDir);
		}

		log.info("Data was successfully parsed");
	}

	/**
	 * parse only one single file
	 * It depends on file extension which type of parser should be used
	 *
	 * @param file
	 */
	private void parseSingleFile(File file) {

		if (FileUtil.getFileType(file) == FileExtension.DOCX) {
			Document dto = executeInThread(file, docxParser);
			parsedData.put(dto.getContentData().getTitle(), Arrays.asList(new Document(), dto));
		} else {
			Document dto = executeInThread(file, pdfParser);
			parsedData.put(dto.getContentData().getTitle(), Arrays.asList(dto, new Document()));
		}
	}

	/**
	 * Parse all docx and pdf files in directory
	 * First parse all pdf files and then parse docx files
	 * By parsing docx files => this files are grouped with pdf files by title
	 *
	 * @param dir
	 */
	private void parseDataInDir(File dir) {

		List<File> pdfFiles = FileUtil.getAllFilesInDir(dir, FileExtension.PDF);
		if (pdfFiles.isEmpty()) {
			log.debug("No files found in dir");
		}

		for (int i = 0; i < pdfFiles.size() && running; i++) {
			Document dto = executeInThread(pdfFiles.get(i), pdfParser);
			fillMap(dto, Arrays.asList(dto, new Document()));
		}

		List<File> docxFiles = FileUtil.getAllFilesInDir(dir, FileExtension.DOCX);
		if (docxFiles.isEmpty()) {
			log.debug("No docx files found in dir");
		}
		for (int i = 0; i < docxFiles.size() && running; i++) {

			Document dto = executeInThread(docxFiles.get(i), docxParser);
			if (dto == null) {
				log.error("File was not parsed. (Please check whether this file is not already open or used by another application): " + docxFiles.get(i));
				continue;
			}

			List<Document> dtos = parsedData.get(dto.getContentData() != null ? dto.getContentData().getTitle() : "");
			if (dtos != null) {
				dtos.set(1, dto);
			} else {
				fillMap(dto, Arrays.asList(new Document(), dto));
			}
		}

	}

	/**
	 * Fill final map with data if title is not find then is used file name
	 *
	 * @param dto
	 * @param dtos
	 */
	private void fillMap(Document dto, List<Document> dtos) {
		if (dto.getContentData().getTitle().isEmpty()) {
			parsedData.put(NONE_VALID_TITLE + dto.getContentData().getFileName(), dtos);
		} else {
			parsedData.put(dto.getContentData().getTitle(), dtos);
		}
	}

	/**
	 * Parsing process is executed in single thread
	 *
	 * @param file
	 * @param parser
	 * @return
	 */
	private Document executeInThread(File file, Parserable parser) {

		Future<Document> future = executor.submit(() -> parser.parse(file));

		try {
			return future.get();
		} catch (InterruptedException e) {
			new ParserException("Execute in Thread problem : " + e);
		} catch (ExecutionException e) {
			new ParserException("Execute in Thread problem : " + e);
		}
		return null;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Map<String, List<Document>> getParsedData() {
		return parsedData;
	}

	public List<Document> getParsedDataAsList() {
		List<Document> resultList = new ArrayList<>();
		for (Map.Entry<String, List<Document>> entry : parsedData.entrySet()) {
			Document pdfFile = entry.getValue().get(0);
			Document docxFile = entry.getValue().get(1);

			resultList.add(DataCombiner.combineData(pdfFile, docxFile));
		}
		return resultList;
	}
}
