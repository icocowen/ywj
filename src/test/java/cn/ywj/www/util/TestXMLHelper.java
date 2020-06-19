package cn.ywj.www.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class TestXMLHelper {
    @Test
    public void shouldReturnUsersDoc() {
        XMLHelper xh = new XMLHelper();
        Document document = null;
        try {
            document = xh.loadUsersDoc();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(document);
    }


    @Test
    public void shouldWriterXMLSucc() {
        XMLHelper xh = new XMLHelper();
        try {
            Document document = xh.loadUsersDoc();
            Element rootElement = document.getRootElement();
            rootElement.addElement("aaa");
            xh.writeUsersDoc(document);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void shouleReturnRandom() {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.toString().replaceAll("-",""));
    }
}
