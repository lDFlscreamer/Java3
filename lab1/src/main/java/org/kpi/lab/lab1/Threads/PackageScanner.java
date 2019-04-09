package org.kpi.lab.lab1.Threads;

import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public class PackageScanner implements Callable<StringBuilder> {

    final static Logger logger = Logger.getLogger(PackageScanner.class);
    @Setter
    ExecutorService pool;

    @Getter
    private StringBuilder data = new StringBuilder();

    @Getter
    @Setter
    private File path;

    @Getter
    @Setter
    private String keyWord;

    public PackageScanner() {
    }

    public PackageScanner(File path, String keyWord, ExecutorService pool) {
        this.path = path;
        this.keyWord = keyWord;
        this.pool = pool;

        logger.info("Object scanner " + this.getClass().getName() + "created\n\t" + "with path:" + this.path.getAbsolutePath() + "\n\tkey :" + this.keyWord);
    }


    public void scann(File file) {

        try (Scanner sc = new Scanner(new FileInputStream(file))) {
            boolean flag = false;
            while (!flag && sc.hasNextLine()) {
                String str = sc.nextLine();


                if (str.contains(keyWord)) {

                    logger.warn("added to data " + file.getName() + " : " + str + "\n");
                    data.append(file.getName() + " : " + str + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }


    }

 public StringBuilder call() throws Exception {
        ArrayList<Future<StringBuilder>> result =
                new ArrayList<>();


        for (File f : path.listFiles()) {


            if (f.isDirectory()) {
                logger.info("\t" + f.getAbsolutePath() + " is a derictory");

                PackageScanner packageScanner =
                        new PackageScanner(f, keyWord, this.pool);

                Future<StringBuilder> rez = pool.submit(packageScanner);
                result.add(rez);

            } else {
                logger.info("\t" + f.getAbsolutePath() + " is a file");
                if (f.getName().contains(".txt")) {
                    scann(f);
                }

            }

        }

        logger.trace("\ndata before :" + data.toString());
        logger.trace("Resul: " + result.toString());


            for (Future<StringBuilder> rez : result) {

                String str = rez.get().toString();
                logger.trace("rez.get()=" + str);
                if (str != null) {
                    data.append(str);
                }

            }

        logger.trace("\ndata  after :" + data.toString());
        return data;

    }


}
