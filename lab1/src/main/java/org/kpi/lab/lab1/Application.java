package org.kpi.lab.lab1;

import org.apache.log4j.Logger;
import org.kpi.lab.lab1.Threads.PackageScanner;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Application {

    final static Logger logger = Logger.getLogger(Application.class);


    public static void main(String[] args) {
        ExecutorService pool = Executors.newSingleThreadExecutor();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter directory -> ");
        String dir = sc.next();
        System.out.print("Enter keyword -> ");
        String keyWord = sc.next();

        System.out.print("Enter path to result file->\n D:\\c++\\JAVA\\lab\\ ");
        String resultPath = "D:\\c++\\JAVA\\lab\\"+sc.next();


        PackageScanner packageScanner =
                new PackageScanner(new File(dir), keyWord, pool);

        Future<StringBuilder> res = pool.submit(packageScanner);
        logger.info(res.toString() + "created");
        try {

            StringBuilder result = res.get();
            File output = new File(resultPath);
            Output.saveResult(output,result );

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
        pool.shutdown();
    }
}
