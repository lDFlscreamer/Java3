package org.kpi.lab.lab1;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Output {

    final static Logger logger = Logger.getLogger(Application.class);

    public static void saveResult(File f, StringBuilder result) {

        try (FileWriter writer = new FileWriter(f, false)) {


            writer.write(result.toString());



            writer.flush();
        } catch (IOException ex) {

            logger.error(ex.getMessage());
        }


    }

}
