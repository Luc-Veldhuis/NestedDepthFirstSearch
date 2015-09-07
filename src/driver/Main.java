package driver;

import java.io.File;
import java.io.FileNotFoundException;

import ndfs.ArgumentException;
import ndfs.NDFS;
import ndfs.NDFSFactory;
import ndfs.ResultException;

/**
 * This is the main program of the NDFS skeleton.
 */
public class Main {

    // Prevent accidental construction.
    private Main() {
        // nothing
    }

    private static void printUsage() {
        System.out.println("Usage: bin/ndfs <file> <version> <nrWorkers>");
        System.out.println("  where");
        System.out.println("    <file> is a Promela file (.prom),");
        System.out.println(
                "    <version> is either \"seq\" or one of the mc versions (for instance, \"1_naive\"),");
        System.out.println(
                "    <nWorkers> indicates the number of worker threads.");
    }

    private static void runNDFS(File promelaFile) throws FileNotFoundException {

        NDFS ndfs = NDFSFactory.createNNDFS(promelaFile);
        long start = System.currentTimeMillis();

        try {
            ndfs.ndfs();
            throw new Error("No result returned by the sequential version");
        } catch (ResultException r) {
            long end = System.currentTimeMillis();
            System.out.println(r.getMessage());
            System.out.println("seq took " + (end - start) + " ms.");
        }
    }

    private static void runMCNDFS(File promelaFile, String version,
            int nrWorkers) throws Exception {

        NDFS ndfs = NDFSFactory.createMCNDFS(promelaFile, nrWorkers, version);
        long start = System.currentTimeMillis();

        try {
            ndfs.ndfs();
            throw new Error("No result returned by " + version);
        } catch (ResultException r) {
            long end = System.currentTimeMillis();
            System.out.println(r.getMessage());
            System.out.println(version + " took " + (end - start) + "  ms.\n");
        }
    }

    private static void dispatch(File promelaFile, String version,
            int nrWorkers) throws ArgumentException, FileNotFoundException {

        if (version.equals("seq")) {
            if (nrWorkers != 1) {
                throw new ArgumentException("seq can only run with 1 worker");
            }
            runNDFS(promelaFile);
        } else {
            try {
                runMCNDFS(promelaFile, version, nrWorkers);
            } catch (FileNotFoundException e) {
                throw e;
            } catch (ArgumentException e) {
                throw e;
            } catch (Throwable e) {
                throw new Error("Could not run version " + version + ": " + e,
                        e);
            }
        }
    }

    /**
     * This is the <code>main</code> method of the NDFS skeleton. It takes three
     * arguments: <br>
     * - a filename of a file containing a Promela program; this describes the
     * graph. <br>
     * - a version string, see
     * {@link NDFSFactory#createMCNDFS(File, int, String)}, but it could also be
     * "seq", for the sequential version. <br>
     * - a number representing the number of worker threads to be used.
     *
     * @param argv
     *            the arguments.
     */
    public static void main(String[] argv) {
        try {
            if (argv.length != 3)
                throw new ArgumentException("Wrong number of arguments");
            File file = new File(argv[0]);
            String version = argv[1];
            int nrWorkers = new Integer(argv[2]);

            dispatch(file, version, nrWorkers);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (ArgumentException e) {
            System.err.println(e.getMessage());
            printUsage();
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            printUsage();
        }
    }
}
