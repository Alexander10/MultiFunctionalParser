# MultiFunctionalParser

JavaFX application for parsing data from scientific papers

# Preconditions
  - JDK 8

# Description
Parser is primarlly used for parsing DOCX and PDF scientific documents. If document has proper structure then parser retrieve 
from document the following parts:
  - Title
  - Subtitle
  - Authors
  - Abstract
  - Keywords
  - References...

In case when exists PDF and also DOCX files for one scientific paper, then parsed data from DOCX and PDF files are merged.

For this parser is also created GUI in JavaFX. GUI serves for easy modification of results also for 
export results to BIB file and serialization of current configuration. 

# References 

  - http://java.dzone.com/articles/fxml-javafx-powered-cdi-jboss
  - http://pdfbox.apache.org/


