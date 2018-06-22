package br.com.etematica.xml;

import java.util.List;

public class Parametros {
    String grupo;
    List<String> xPathFile;

    public Parametros(String grupo, List<String> xPathFile) {
        this.grupo = grupo;
        this.xPathFile = xPathFile;
    }

    public Parametros() {

    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public List<String> getxPathFile() {
        return xPathFile;
    }

    public void setxPathFile(List<String> xPathFile) {
        this.xPathFile = xPathFile;
    }
}
