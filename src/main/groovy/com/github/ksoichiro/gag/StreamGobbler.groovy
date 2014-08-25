package com.github.ksoichiro.gag

class StreamGobbler extends Thread {

    BufferedReader reader;

    public StreamGobbler(InputStream is) {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        while (reader.readLine() != null) {
            // Just drop contents
        }
    }

}
