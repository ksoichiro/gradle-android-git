package com.github.ksoichiro.gag

class StreamGobbler extends Thread {

    BufferedReader reader;

    public StreamGobbler(InputStream is) {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        String line
        while ((line = reader.readLine()) != null) {
            // Just drop contents
        }
    }

}
