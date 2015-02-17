package sk.ban.data;

import sk.ban.enums.FileExtension;

import java.io.Serializable;

/**
 * Created by USER on 26. 1. 2015.
 */
public class Document implements Serializable {

	private DocumentNavigation additionalData;
	private DocumentContent contentData ;

	public Document(){

	}
	public Document(DocumentNavigation additionalData, DocumentContent contentData) {
		this.additionalData = additionalData;
		this.contentData = contentData;
	}

	public boolean hasContentData(){
		return contentData != null;
	}

	public boolean hasNavigationData(){
		return additionalData != null;
	}
	public DocumentNavigation getAdditionalData() {
		return additionalData;
	}

	public DocumentContent getContentData() {
		return contentData;
	}

	@Override
	public String toString() {
		return "DocumentData{" +
				"additionalData=" + additionalData != null ? additionalData.toString() : "" +
				", contentData=" + contentData != null ? contentData.toString() :"" +
				'}';
	}
}
