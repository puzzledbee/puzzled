/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import puzzled.data.Category;
import puzzled.data.Item;
import puzzled.data.LogicProblem;
import puzzled.processor.Parser;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class PuzzledFileIO {
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());    

    //converts a string filename into a File object, invokes other static method
    public static LogicProblem loadProblem(String myFile) throws JAXBException, IOException {
        return PuzzledFileIO.loadProblem(new File(myFile));
    }
    
    /*
    * This routine loads a logic problem file by de-serializing
    * the object from file
    * @param myFile string representing the filename to be loaded.
    */
    //throwing exception here, so that the calling code in the PuzzledController
    //can "notify" the user graphically in case of issues.
    public static LogicProblem loadProblem(File file) throws JAXBException, IOException {
        //check that there are no problem already loaded
        fLogger.log(Level.INFO, "loading logic problem file: " + file.getName());
        LogicProblem newProblem = null;
        String extension = file.getName().replaceAll("^.*\\.([^.]+)$", "$1");
        if (extension.equalsIgnoreCase("lpf")) {
            JAXBContext jaxbContext = JAXBContext.newInstance(LogicProblem.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            newProblem = (LogicProblem) jaxbUnmarshaller.unmarshal(file);
            fLogger.log(Level.INFO, newProblem.toString());
            return newProblem;
        } else if (extension.equalsIgnoreCase("lps")) {
//            System.out.println("trying to load Logic Problem Shorthand");
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
//                System.out.println("problem "+lines.get(0)+" with "+ lines.size());
//                System.out.println("last line "+lines.get(lines.size()-1));

            //using the VARARG constructor to feed all relevant puzzle information
            newProblem = new LogicProblem(lines.get(0).split(";"));
            int i=1;
            int catIndex=1;
            Boolean problemTextToggle = false;
            String problemText = "";
            while (i < lines.size()) {
                if (i < lines.size()-1 && lines.get(i+1).startsWith("\t")) {
                    i++;
                    ArrayList<Item> items = new ArrayList<Item>();
                    while (i < lines.size() && lines.get(i).startsWith("\t")) {
                        System.out.println("adding item "+lines.get(i).trim()+" ("+i+")");
                        items.add(new Item(lines.get(i++).trim()));
                    }
//                        System.out.println("adding category "+lines.get(catIndex)+" ("+catIndex+")");
                    String[] catInfo = lines.get(catIndex).split(";");
                    Category newCat = new Category(items,catInfo);
                    catIndex = i;
                    newProblem.addCategory(newCat);
                } else {
                    if (i<lines.size()) {
                        if (lines.get(i).trim().isEmpty()) {
                            problemTextToggle = true; 
                            i++; //skip over blank line
                        } else {
                            if (problemTextToggle) {
                                System.out.println("appending text "+lines.get(i));
                                problemText += lines.get(i++) + "\n";
                            } else {
                                System.out.println("adding clue "+lines.get(i)+" ("+i+")");
                                String[] clueInfo = lines.get(i++).split(";");
                                //Clue newClue = new Clue(clueInfo);
                                Parser.parse(newProblem,clueInfo[0]);
                                //newProblem.getNumberedClueList().addMajorClue(newClue); 
                            }
                        }
                    } //else there are no clues present
                }
            }
            if (!problemText.trim().isEmpty()) newProblem.setProblemText(problemText);
            return newProblem;
        } else return null;
    }
    
    public static void loadClues(File file, LogicProblem logicProblem) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                for (String line : lines) {
                    String[] clueInfo = line.split(";");
                    //Clue newClue = new Clue(clueInfo);
                    Parser.parse(logicProblem, clueInfo[0]);
                    //logicProblem.get().getNumberedClueList().addMajorClue(newClue); //this will parse the clue
                    logicProblem.setDirtyFile(true);
            }
    }
    
    
    public static void saveFile(String filename, LogicProblem logicProblem) throws JAXBException {
        File file = new File(filename);
        JAXBContext jaxbContext = JAXBContext.newInstance(LogicProblem.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

//                    LogicProblem newProblem = DemoProblems.generateDemoProblem47();
        jaxbMarshaller.marshal(logicProblem, file);
//        jaxbMarshaller.marshal(logicProblem, System.out);
        logicProblem.dirtyFileProperty().set(false);
    }
    
}
