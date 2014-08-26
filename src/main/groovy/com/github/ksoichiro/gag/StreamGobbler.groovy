package com.github.ksoichiro.gag

class StreamGobbler extends Thread {

    BufferedReader reader;

    public StreamGobbler(InputStream is) {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        // String s causes NoClassDefFoundException for ShortTypeHandling
        def s
        while ((s = this.reader.readLine()) != null) {
            // Just drop contents
            println(s)
        }
    }

}
