package xml_to_csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.*;
import org.bouncycastle.util.Store;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/*
 * https://www.bouncycastle.org/latest_releases.html
 * installato v.JDK 1.8 JAR files pi� recenti
 * 
 * 
 * ---EN-----------------------------------------------------------
 * GOAL:
 * Open folder containing the relevant files (xml and p7m)
 * Iterate over each file by extension:
 * Decrypt signed files using the Bouncy Castle library.
 * Finally, process the root (Element) that contains all the XML files and convert it while adhering to the formatting of the stylesheet (style.xml).
 * Additionally, create a folder named "Allegati_PDF" (using the PdfGenerator class) to save the attachments of each invoice or create it if it does not exist.
 * ---IT-----------------------------------------------------------
 * GOAL:
 * Apro cartella contenente i file interessati (xml e p7m)
 * Ciclo ogni file per estensione:
 * quelli firmati vanno decifrati con lib bouncycastle.
 * Infine passo root (Element) che contiene tutti gli XML e lo converto rispettando la formattazione dello stylesheet (style.xml).
 * Inoltre creo cartella Allegati_PDF (usando classe PdfGenerator) per salvare gli allegati di ogni fattura o crearlo se non esiste.
 * 
 * Input Stream Management for Stylesheet File � gestito dal metodo (molto UTILE!) void getFileFromResource(String fileName, File ff) throws URISyntaxException, IOException{}
 * 
 * Infondo sono presenti due metodi di stampa per testing.
 * 
 * GU 8/11/22
 * 
 */

public class P7mDecoder {
	public static Object syncObject = null;
	public static File folder = null; // per testing new File("C:/Users/user1/Desktop/esempiXML")
	public static File stylesheet = new File("style.xml"); //file posizionato nella cartella resources //new File("src/resources/style.xml");
	public static int counter = 0;

	@SuppressWarnings({ "unchecked", "unused" })
	public static void main(String[] args) throws TransformerFactoryConfigurationError, 
	DOMException, Exception {
		getFileFromResource("style.xml", stylesheet); //stylesheet
		
		// chiamo classe per aprire Folder Chooser all'utente e selezionare cartella interessata contenente files XML
        try {
			JavaFileChooser.createAndShowGUI();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		       
		if(folder == null || folder.listFiles() == null) // Terminate JVM because user selected wrong args
			System.exit(-1);//execution went wrong		
		
		
        File[] listOfFiles = folder.listFiles();
        for(File f : listOfFiles)
        	System.out.println("---Testing file trovato = " + f.toString());
        
        Document document = null;
        Element root = null;
        Boolean t = true;
        
        Pattern p = Pattern.compile("([0-9]{1,}\\_)?[a-zA-Z]{2}[0-9]{5,}\\_[0-9a-zA-Z]{1,}\\.(xml|XML|xml.p7m|XML.p7m|XML.P7M|xml.P7M)$");
        
        for(int i = 0; i < listOfFiles.length; i++){
            String filename = listOfFiles[i].getName();
            Matcher m = p.matcher(filename);

            if((filename.endsWith(".xml.p7m") || filename.endsWith(".XML.p7m")) && 
        		m.matches() 
        		) {
            	// --------------- caso in cui trovo file cifrati .xml.P7M 
            	System.out.println("file p7m inserito: " + filename.toString());
            	
                File xmlSourceFile = new File(folder.toString() +"/"+ filename); //your XML data signed P7M                
                String p7mEncrFile = folder.toString() +"/"+ filename; //your XML data             
                Path path = Paths.get(p7mEncrFile);
                byte[] data = Files.readAllBytes(path);
            
                // Corresponding class of signed_data is CMSSignedData
                // inizio processo di decriptazione del file P7M
                CMSSignedData signature = new CMSSignedData(data);
                Store<?> cs = signature.getCertificates();
                SignerInformationStore signers = signature.getSignerInfos();
				Collection<?> c = signers.getSigners();
				Iterator<?> it = c.iterator();               
                // the following array will contain the content of xml document
                data = null;                
                File DecryptedFileXml = new File("DecryptedFileXml.xml");                              
                while (it.hasNext()) {
                     SignerInformation signer = (SignerInformation) it.next();
                     Collection<?> certCollection = cs.getMatches(signer.getSID());
                     Iterator<?> certIt = certCollection.iterator();
                     X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
                     CMSProcessable sc = signature.getSignedContent();
                     data = (byte[]) sc.getContent();
                     try (FileOutputStream outputStream = new FileOutputStream(DecryptedFileXml)) {
                         outputStream.write(data);
                     }                  
                     System.out.println(" - - - - File Decriptato: " + filename);
                }
                //cambio puntatore perch� ho convertito
                xmlSourceFile = DecryptedFileXml; 
                
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            
                if(t) {             	
    	            Document newdoc = builder.parse(xmlSourceFile);
    	        	document = newdoc;           	 
	                root = document.getDocumentElement();
	                                              
	                Node firstDocImportedNode = document.importNode(root, true);
	                root.appendChild(firstDocImportedNode);
	                counter++;	                
	                PrintPDFfromAttachment(xmlSourceFile, filename); // stampa PDF presente nell'XML
	                t=false;//ho aggiunto primo elem quindi ora ho un elem nel doc
                } else {                
	                Node firstDocImportedNode = document.importNode(addSecondNodeToXML(xmlSourceFile, document), true);
	                root.appendChild(firstDocImportedNode);	         
	                counter++;
	                PrintPDFfromAttachment(xmlSourceFile, filename); // stampa PDF presente nell'XML
                }
            } else if ((filename.endsWith(".xml") || filename.endsWith(".XML")) && 
            		m.matches() 
            		) {
               	// --------------- caso in cui trovo un classico XML non cifrato
            	System.out.println("file XML inserito: " + filename.toString());
                File xmlSourceFile = new File(folder.toString() +"/"+ filename); //your XML data                
            	
            	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
                
	            if(t) {
	            	Document newdoc = builder.parse(xmlSourceFile);
    	        	document = newdoc;           	 
	                root = document.getDocumentElement();
	               	                               
	                Node firstDocImportedNode = document.importNode(root, true);	                
	                root.appendChild(firstDocImportedNode);	
	                counter++;
	                System.out.println(">>>>PrintPDFfromAttachment:  "+filename.toString()+" "+xmlSourceFile.exists());//da togliere
	                PrintPDFfromAttachment(xmlSourceFile, filename); // stampa PDF presente nell'XML
	                t=false;//ho aggiunto primo elem quindi ora ho un elem nel doc
                } else {
                	Node firstDocImportedNode = document.importNode(addSecondNodeToXML(xmlSourceFile, document), true);	     
	                root.appendChild(firstDocImportedNode);	 	                
	                counter++;
	                PrintPDFfromAttachment(xmlSourceFile, filename); // stampa PDF presente nell'XML
                }
            }           
		    //prettyPrint(root);
		    //prettyPrintNodeList(root.getChildNodes());	            
        }//END ciclo for
        	
        StreamSource stylesource = new StreamSource(stylesheet); //file di formattazione per trasformarlo in CSV
        Transformer transformer2 = TransformerFactory.newInstance().newTransformer(stylesource);
        Source s = new DOMSource(root);
          
        String today = java.time.LocalDate.now().toString();
        Result outputTarget = new StreamResult(folder.toString() +"/"+ new File(today +"-RiepilogoFatture.csv")); //creo csv vuoto
        transformer2.transform(s, outputTarget); //ultimo passo - converte XML con stile nel CSV creato
        
        //prettyPrint(root);
        System.out.println("--------------------------------------------------------------csv creato: " + today +"-RiepilogoFatture.csv CONTATORE FILE === " + counter);
        stylesheet.delete();
        
        System.out.println("--------------------------------------file aux cancellati!");
        System.exit(0);//execution went fine
	}
	
	
	
	/**
	 * Scrivere metodo generale da mettere nell'ifelse in modo che funzioni anche su primo file (se non unico)
	 * ok controllo su tag attachment 
	 * conveniente creare cartella e poi all'interno salvare tutti i file pdf trovati
	 */
	public static void PrintPDFfromAttachment (File xmlSourceFile, String filename) throws Exception {
		System.out.println("-------PrintPDFfromAttachment------");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
		Document newdoc = builder.parse(xmlSourceFile);
		
		/* Stampa contenuto Document ----
		newdoc.getDocumentElement().normalize();
	    System.out.println("Root element " + newdoc.getDocumentElement().getNodeName());
	    NodeList nodeList=newdoc.getElementsByTagName("*");
	    for (int i=0; i<nodeList.getLength(); i++) {
	        // Get element
	        Element element = (Element)nodeList.item(i);
	        System.out.println("i= " + i + " " + element.getNodeName());
	    }
	     */
	    Element ee = null;
	    NodeList nodeL = newdoc.getElementsByTagName("Attachment");// prendo NodeList di elementi con il tag trovato (se esiste senn� NULL)
	    // FatturaElettronicaBody>Data e Numero
	    // CedentePrestatore>DatiAnagrafici>CodiceFiscale o PARTITA IVA!
	     
	    if(nodeL != null && nodeL.getLength() > 0) { //se c'� l'attachment lo salvo, altrimenti lo creo
	    	ee = (Element)nodeL.item(0); //prendo primo elemento della lista
	    	System.out.println(">>>"+"Allegato PDF trovato nel file "+filename+", lo salvo!");
	    	System.out.println(">>>Decoding = " + ee.getTextContent());
	    	byte[] decoded = null;
	    	try {
	    		decoded = Base64.getDecoder().decode(ee.getTextContent());
	    	} catch (Exception e) {
	    		System.out.println(e);
	    		return;
	    	}
	    	
	    	//String decodedStr = new String(decoded, StandardCharsets.UTF_8);
	    	
	    	String CF_CedentePrestatore = null;
	    	NodeList nodeL_CF = newdoc.getElementsByTagName("CodiceFiscale");
	    	//System.out.println("FOR CODICE FISCALE");
	    	if(nodeL_CF.getLength() > 0) {
	    		Element element = (Element)nodeL_CF.item(0);
	    		CF_CedentePrestatore = element.getTextContent();
	    		if(CF_CedentePrestatore.length() == 16) {
	    			//cerco nome e cognome perch� persona fisica
	    			CF_CedentePrestatore =  CF_CedentePrestatore +"_"+newdoc.getElementsByTagName("Nome").item(0).getTextContent().toString();
	    			CF_CedentePrestatore =  CF_CedentePrestatore +"_"+newdoc.getElementsByTagName("Cognome").item(0).getTextContent().toString();
	    		} else {
	    			//cerco Denominazione perch� persona giuridica
	    			CF_CedentePrestatore =  CF_CedentePrestatore +"_"+newdoc.getElementsByTagName("Denominazione").item(0).getTextContent().toString().strip().substring(0, 10);
	    		}
	    	} 
	    		
	    	/*
	    	for (int i=0; i<nodeL_CF.getLength(); i++) {
		        // Get element
		        Element element = (Element)nodeL_CF.item(i);
		        System.out.println("i= " + i + " NodeName= " + element.getNodeName() + " NodeValue= " + element.getNodeValue() + " NodeTextContent= " + element.getTextContent()+ "  "+ nodeL_CF.getLength());
		        CF_CedentePrestatore = element.getTextContent();
	    	}
	    	*/
	    	String nFatture_Data = null;
	    	nodeL_CF = newdoc.getElementsByTagName("Numero");
	    	//System.out.println("---FOR Numero");
	    	if(nodeL_CF.getLength() > 0) {
	    		Element element = (Element)nodeL_CF.item(0);
	    		nFatture_Data = element.getTextContent().toString();
	    	}
	    	nodeL_CF = newdoc.getElementsByTagName("Data");
	    	//System.out.println("---FOR data");
	    	if(nodeL_CF.getLength() > 0) {
	    		Element element = (Element)nodeL_CF.item(0);
	    		nFatture_Data = nFatture_Data +"_"+ element.getTextContent().toString();
	    	}
	    	//System.out.println("---nFatture_Data= " + nFatture_Data);
	    	
	    	// Sending your output through a FileWriter is corrupting it because the data is bytes, and FileWriters are for writing characters.
	    	Files.createDirectories(Paths.get(folder.getPath()+"/Allegati_PDF"));
	    	OutputStream out = new FileOutputStream(folder.getPath()+"/Allegati_PDF/" + CF_CedentePrestatore+"_"+nFatture_Data+"_"+filename.toString() + ".pdf"); //rinominare in:: CFpalazzo_nomefornitore_n fattura_ data.
	    	out.write(decoded);//vuole byte[]
	    	out.close();
	    } else {
	    	System.out.println(">>>"+filename.toString() + " allegato non trovato, lo genero!");
	    	Files.createDirectories(Paths.get(folder.getPath()+"/Allegati_PDF"));
	    	// chiamo classe per generare PDF: passo file xml e Dir dove salvare in memoria
	    	PdfGenerator.main(xmlSourceFile, filename.toString());
	    }		
	}
	
	
	@SuppressWarnings("exports")
	public static Node addSecondNodeToXML(File xmlSourceFile, Document document) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
		Document newdoc = builder.parse(xmlSourceFile);
    	return newdoc.getLastChild();
	}
	
	
	/* ------------- Input Stream Management for Stylesheet File ---------------------------------------------- */
    /*
        The resource URL is not working in the JAR
        If we try to access a file that is inside a JAR,
        It throws NoSuchFileException (linux), InvalidPathException (Windows)

        Resource URL Sample: file:java-io.jar!/json/file1.json
     */
    public static void getFileFromResource(String fileName, File ff) throws URISyntaxException, IOException{
        InputStream resource = P7mDecoder.class.getResourceAsStream(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! getFileFromResource() (probabilmente nel path diverso da quello di questa classe) " + fileName);
        } else {
        	System.out.println(fileName + " File trovato!");
        	FileUtils.copyInputStreamToFile(resource, ff);
        }
    }
	
	
	
	/* -------------TESTING--------------------------------------------------------------- */
	// stampa in modo leggibile un DOC
	public static final void prettyPrint(@SuppressWarnings("exports") Element root) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(root), new StreamResult(out));
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        writer.println(out.toString());
        writer.close();
        //System.out.println(out.toString());
    }
	
	// stampa tutti gli elementi di un NodeList
	public static final void prettyPrintNodeList(@SuppressWarnings("exports") NodeList nl) throws Exception {
    	int length = nl.getLength();
    	for (int j = 0; j < length; j++) {
    		Node el = nl.item(j);
    		System.out.println(j + "---" + el);              	
    	}
    	System.out.println("---------end prettyPrintNodeList--");
    }
	

}

