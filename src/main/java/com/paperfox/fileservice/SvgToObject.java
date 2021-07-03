package com.paperfox.fileservice;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.List;

public class SvgToObject {
    public void getSvgObject(String svgFile) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SVG.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(svgFile);
            SVG svg = (SVG) jaxbUnmarshaller.unmarshal(reader);

//            JAXBElement<SVG> elemento = (JAXBElement<SVG>)jaxbUnmarshaller.unmarshal(reader);
//            SVG object = elemento.getValue();

            System.out.println(svg);
//            List<Path> list = svg.getPathList();
//            for (Path path : list)
//                System.out.println(path.getDesc());

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
