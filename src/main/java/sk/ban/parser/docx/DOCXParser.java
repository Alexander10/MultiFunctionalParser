package sk.ban.parser.docx;

import sk.ban.enums.DocumentPart;
import sk.ban.exception.ParserException;
import sk.ban.data.Document;
import sk.ban.data.DocumentContent;
import sk.ban.data.DocumentNavigation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sk.ban.parser.Parserable;
import sk.ban.util.FileUtil;
import sk.ban.worker.DataCleaner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/**
 * Docx has a following structure
 * At first algorithm find w:pStyle element and check value of w:wal attribute
 * If value is positive (one of the following: References, Author, Abstract ....) the algorithm
 * has to go to grandparent (w:p element) and from this position traverses all w:t elements and fetches all values
 * <p>
 * <w:p w:rsidP="00B01315" w:rsidRDefault="0066192A" w:rsidRPr="00F26469" w:rsidR="0066192A">
 * <w:pPr>
 * <w:pStyle w:val="papertitle"/>
 * </w:pPr>
 * <w:r w:rsidRPr="00F26469">
 * <w:t>
 * God, the Creator of the multiverse
 * </w:t>
 * </w:r>
 * </w:p>
 * <p>
 * <p>
 * <p>
 * Created by USER on 21. 1. 2015.
 */
public class DOCXParser implements Parserable {

	private static final Logger log = LoggerFactory.getLogger(DOCXParser.class);

	/**
	 * Xml document contains element w:pStyle which has a value of formatting or section name
	 *
	 * @param file - which we want to parse
	 */
	public Document parse(File file) {

		log.debug("Start parsing docx file: " + file.getName());
		Document newDocument = new Document(new DocumentNavigation(), new DocumentContent());
		DocumentContent dto = newDocument.getContentData();
		dto.setFileName(file.toString());

		InputStream in = FileUtil.openDocx(file).orElseThrow(() -> new ParserException(""));

		org.w3c.dom.Document doc = createDOMDocument(in);
		//fetch all elements
		NodeList list = doc.getElementsByTagName("*");
		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);
			if (isValidNode(node)) {

				DocumentPart docPart = DocumentPart.fromString(getFirstAttributeValue(node));
				String text = fetchTextFromNode(node);

				if (text.isEmpty()) {
					continue;
				}

				switch (docPart) {
					case ABSTRACT:
						dto.setAbstractText(text);
						break;
					case AUTHOR:
						dto.addAuthor(text);
						break;
					case REFERENCES:
						dto.addReference(text);
						break;
					case KEYWORDS:
						dto.addKeyword(text);
						break;
					case SUBTITLE:
						if(dto.getSubtitle().isEmpty()) {
							dto.setSubtitle(text);
						}
						break;
					case TITLE:
						if(dto.getTitle().isEmpty()) {
							dto.setTitle(text);
						}
						break;
					default:
						//should be never reached
						break;
				}
			}
		}
		DataCleaner.cleanData(dto);
		log.info("DOCX file was successfully parsed: " + file.toString());
		return newDocument;
	}

	/**
	 * Node w:pStyle contains attribute with value (Abstract, References, ....)
	 *
	 * @param node
	 * @return
	 */
	private boolean isValidNode(Node node) {
		return node.getNodeType() == Node.ELEMENT_NODE && "w:pStyle".equals(node.getNodeName()) && DocumentPart.contains(getFirstAttributeValue(node));
	}

	private String fetchTextFromNode(Node node) {

		Node grandParent = getGrandParent(node);
		NodeList childNodes = grandParent.getChildNodes();
		String nodeText = "";
		for (int i = 0; i < childNodes.getLength(); i++) {

			if (isDataElement(childNodes.item(i))) {

				NodeList textChilds = childNodes.item(i).getChildNodes();
				for (int j = 0; j < textChilds.getLength(); j++) {
					nodeText += textChilds.item(j).getTextContent();
				}
			}
		}
		return nodeText;
	}

	/**
	 * w:r element contains w:t element where are placed data
	 *
	 * @param node
	 * @return
	 */
	private boolean isDataElement(Node node) {
		return "w:r".equals(node.getNodeName());
	}

	private String getFirstAttributeValue(Node node) {
		return node.getAttributes().item(0).getNodeValue();
	}

	private Node getGrandParent(Node node) {
		return node.getParentNode().getParentNode();
	}


	/**
	 * Creates DOM document for parsing data
	 *
	 * @param in
	 * @return
	 */
	private org.w3c.dom.Document createDOMDocument(InputStream in) {

		org.w3c.dom.Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(in);
		} catch (ParserConfigurationException e) {
			log.error("Docx Parser configuration problem Document builder problem: " + e);
		} catch (SAXException e) {
			log.error("DOCX: " + e);
		} catch (IOException e) {
			log.error("DOCX: " + e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				throw new ParserException("Problem with closing input stream: " + e);
			}
		}

		log.debug("Document was successfully created");

		return doc;
	}
}
