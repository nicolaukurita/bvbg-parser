package br.com.etematica.xml;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.*;

public class Main {

    // the input file location
    private static final String fileLocation = "D:\\Salvar\\ipn\\captura alocacao e repasse\\10. Arquivos\\BVBG.013.02.xml";
    private static final String fileName = "D:\\Salvar\\Desenvolvimento\\Java\\spring\\integration\\bvbg-parser\\src\\main\\resources\\\\BVBG.013.02.out.txt";
    // the target elements
    private static final String BIZMSGIDR = "TckrSymb";

    public static void main(String[] args) throws IOException, XMLStreamException {
        Stack xPath = new Stack();
        List<String> campos = new ArrayList();
        Parametros parametros = new Parametros();
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        parametros = LoadXPath("D:\\Salvar\\Desenvolvimento\\Java\\spring\\integration\\bvbg-parser\\src\\main\\resources\\bvbg.013.02.txt");

        for (int i = 0; i < parametros.xPathFile.size(); i++) {
            campos.add("");
        }

        int indice = 0;

        FileInputStream fileInputStream = new FileInputStream(fileLocation);
        XMLStreamReader xmlr = XMLInputFactory.newInstance().createXMLStreamReader(
                fileInputStream);

        while (xmlr.hasNext()) {

            int eventCode = xmlr.next();
            switch (eventCode) {
                case XMLStreamConstants.START_ELEMENT:
                    xPath.push(xmlr.getLocalName());
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (isSame(xPath, parametros.grupo)) {
                        gravar(writer, campos);
                    }
                    xPath.pop();
                    break;
                case XMLStreamConstants.SPACE:
                case XMLStreamConstants.CHARACTERS:
                    indice = isCapture(xPath, parametros.xPathFile);
                    if (indice >= 0) {
                        int start = xmlr.getTextStart();
                        int length = xmlr.getTextLength();
                        campos.set(indice, new String(xmlr.getTextCharacters(),
                                start,
                                length));
                    }
                    break;
                case XMLStreamConstants.PROCESSING_INSTRUCTION:
                    break;
                case XMLStreamConstants.CDATA:
                    break;
                case XMLStreamConstants.COMMENT:
                    break;
                case XMLStreamConstants.ENTITY_REFERENCE:
                    break;
                case XMLStreamConstants.START_DOCUMENT:
                    break;
            }
            System.out.println(Arrays.toString(xPath.toArray()));
        }

        writer.close();
    }

    private static void gravar(BufferedWriter writer, List<String> campos) throws IOException {
        String delimitador = "";
        for (int i = 1; i < campos.size(); i++) {
            writer.write(delimitador + campos.get(i));
            delimitador = ",";
        }
        writer.write("\n");
    }

    private static boolean isSame(Stack xPath, String grupo) {
        String xPathAtual = Arrays.toString(xPath.toArray());

        return xPathAtual.equalsIgnoreCase(grupo);
    }

    private static int isCapture(Stack xPath, List<String> xPathFile) {
        String xPathAtual = Arrays.toString(xPath.toArray());

        System.out.println("-" + xPathAtual);
        return xPathFile.indexOf(xPathAtual);
    }

    private static Parametros LoadXPath(String fileName) {
        File file = new File(fileName);
        Scanner input = null;
        String grupo = "";

        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<String> list = new ArrayList<String>();

        while (input.hasNextLine()) {
            String aux = input.nextLine();
            int posInicial = aux.indexOf("/");
            String tipo = aux.substring(0, posInicial);
            aux = aux.substring(posInicial);

            aux = aux.replace("/", ", ");
            aux = "[" + aux.substring(2) + "]";

            if (tipo.equalsIgnoreCase("grupo")) {
                grupo = aux;
            }

            list.add(aux);
        }

        Parametros parametros = new Parametros();
        parametros.xPathFile = list;
        parametros.grupo = grupo;
        return parametros;
    }
}
