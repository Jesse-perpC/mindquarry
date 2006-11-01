package com.mindquarry.jcr.changes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

public class Change {
    public static void main(String[] args) throws Exception {
        ClientRepositoryFactory factory = new ClientRepositoryFactory();
        Repository repo = factory
                .getRepository("rmi://localhost:1099/jackrabbit");
        Session session = repo.login();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        session.exportDocumentView("/", bos, false, false); //$NON-NLS-1$

        // JAXP reads data using the Source interface
        Source xmlSource = new StreamSource(new ByteArrayInputStream(bos
                .toByteArray()));
        Source xsltSource = new StreamSource(new File("change.xslt"));

        ByteArrayOutputStream changeResult = new ByteArrayOutputStream();
        
        // the factory pattern supports different XSLT processors
        TransformerFactory transFact = TransformerFactory.newInstance();
        Transformer trans = transFact.newTransformer(xsltSource);
        trans.transform(xmlSource, new StreamResult(changeResult));

        DocumentBuilderFactory tmp = DocumentBuilderFactory.newInstance();
        tmp.setNamespaceAware(true);
        DocumentBuilder builder = tmp.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(bos.toByteArray()));

        OutputFormat format = new OutputFormat();
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);

        XMLSerializer serializer = new XMLSerializer(System.out, format);
        serializer.serialize(doc);

    }
}
