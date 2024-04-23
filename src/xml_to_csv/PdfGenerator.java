package xml_to_csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;


/*
 * ok genera pdf
 * fare testing
 * formattare pdf prodotto con XSL -> vedo file resources/templatePDF.xsl
 */

public class PdfGenerator {

	public static void main(File xmlResource, String filename) throws URISyntaxException {
		// TODO Auto-generated method stub
		try {
            convertToPDF(xmlResource, filename);
        } catch (FOPException | IOException | TransformerException e) {
            e.printStackTrace();
        }
	}
	
	public static void convertToPDF(File xmlResource, String filename) throws IOException, FOPException, TransformerException, URISyntaxException {
        File xsltFile = new File("templatePDF.xsl"); // posizionato alla stessa altezza del .java che lo usa! vedi metodo getFileFromResource per JAR
        
        P7mDecoder.getFileFromResource("templatePDF.xsl", xsltFile);
        
        if(xsltFile != null)
        	System.out.println(">>>"+"FILE templatePDF.xsl per conversione a PDF TROVATO");
        StreamSource xmlSource = new StreamSource(xmlResource);
        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // Setup output -- bisogna poi creare cartella comune per tutti i pdf
        OutputStream out;
        out = new FileOutputStream(P7mDecoder.folder + "/Allegati_PDF/"+filename+".pdf");
        // https://stackoverflow.com/questions/212577/how-do-you-create-a-pdf-from-xml-in-java
        try {
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            // Resulting SAX events (the generated FO) must be piped through to
            // FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then
            // PDF is created
            transformer.transform(xmlSource, res); //genera errore document empty: cioè capisce che genera un pdf con errore
        } finally {
            out.close();
            xsltFile.delete();
            System.out.println(">>> "+"PdfGenerator---Generato PDF per "+filename);       
        }
    }

}
