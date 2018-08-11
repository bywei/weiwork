package cn.bywei.weixin.common.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JaxbUtils {

	private JaxbUtils() {}

	@SuppressWarnings("unchecked")
	public static <T> T convertToObject(String xml, Class<T> type) {
		StringReader sr = new StringReader(xml);
		try {
			JAXBContext jAXBContext = JAXBContext.newInstance(type);
			Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
			return (T) unmarshaller.unmarshal(sr);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

	}

	public static String convertToXmlString(Object source) {
		try {
			StringWriter sw = new StringWriter();
			JAXBContext jAXBContext = JAXBContext.newInstance(source.getClass());
			Marshaller marshaller = jAXBContext.createMarshaller();
			marshaller.marshal(source, sw);
			return sw.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
}
