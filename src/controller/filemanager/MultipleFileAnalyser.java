package controller.filemanager;

import java.io.File;
import java.util.*;

import controller.Controller;
import exceptions.InvalidEdgeException;
import interfaces.ITextWindow;
import model.DataModel;

/**
 * Der MultipleFileAnalyser lädt mehrere Dateien, erzeugt für jede ein
 * gesondertes {@link DataModel} und {@link Controller}. Jeder
 * {@link Controller} errechnet ob das entsprechende Petrinetz beschränkt ist
 * und gibt das Ergebnis über ein übergebenes {@link ITextWindow} aus. Da diese
 * Berechnungen bei sehr vielen übergebenen Dateien lange dauern können ist es
 * als eigener Thread implementiert um andere Prozesse nicht zu behindern.
 *
 */
class MultipleFileAnalyser implements Runnable {
    private final List<File> fileList;
    private final ITextWindow txtWindow;

    /**
     * Konstruktor
     * 
     * @param fileArray die zu analysierenden Dateien
     * @param txtWindow ein TextWindow zur Ausgabe der Analyse Ergebnise
     */
    protected MultipleFileAnalyser(File[] fileArray, ITextWindow txtWindow) {
        this.fileList = createListfromArrayAndSortItByName(fileArray);
        this.txtWindow = txtWindow;
    }

    private ArrayList<File> createListfromArrayAndSortItByName(File[] fileArray) {
        ArrayList<File> list = createListfromArray(fileArray);
        sortListByName(list);
        return list;
    }

    private ArrayList<File> createListfromArray(File[] fileArray) {
        ArrayList<File> list = new ArrayList<File>(fileArray.length);
        Collections.addAll(list, fileArray);
        return list;
    }

    private void sortListByName(ArrayList<File> list) {
        list.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    private void analyseFiles() {
        StringBuilder stringbuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringbuilder, Locale.GERMANY);
        createHeader(stringbuilder, formatter);
        for (File file : fileList) {
            txtWindow.pushText(file.getName() + " wird geladen. \n");
            loadFileValidateAndWriteResultsToFormatter(formatter, file);
        }
        txtWindow.pushText("\n");
        txtWindow.pushText(stringbuilder.toString());
        formatter.close();
    }

    private void createHeader(StringBuilder sb, Formatter formatter) {
        formatter.format("%1$-55s | %2$-10s | %3$-50s \n", "", "", "Knoten / Kanten");
        formatter.format("%1$-55s | %2$-10s | %3$-50s \n", "Dateiname : ", "beschränkt", "Pfadlänge:Pfad; m, m'");
        sb.append(
                "---------------------------------------------------------------------------------------------------------------------------------------\n");
    }

    private void loadFileValidateAndWriteResultsToFormatter(Formatter formatter, File file) {
        PNMLWopedParserWithBuffer parser = new PNMLWopedParserWithBuffer(file);
        try {
            parser.initParser();
            parser.parse();
            try {
                DataModel model = parser.flushBufferAndCreateModel();
                Controller controller = new Controller(model);
                controller.addTextWindowListener(txtWindow);
                controller.validateAccessabilityGraph(true, false);
                String isLimitedText = model.getAccessabilityGraph().getIsValid() ? "ja" : "nein";
                // Zeile der Tabelle hinzufügen
                formatter.format("%1$-55s | %2$-10s | %3$-50s \n", file.getName(), isLimitedText,
                        model.getAccessabilityGraph().getValidityRepresentation());
            } catch (InvalidEdgeException e) {
                txtWindow.pushText(file.getName() + ": Model konnte nicht erzeugt werden");
            }
        } catch (Exception e) {
            txtWindow.pushText(file.getName() + " : Datei konnte nicht geladen werden.");
        }
    }

    /**
     * Überprüft alle übergebenen Dateien auf Unbeschränktheit und gibt das Ergebnis
     * auf dem übergebenen ITextWindow aus.
     */

    @Override
    public void run() {
        analyseFiles();
    }

}
