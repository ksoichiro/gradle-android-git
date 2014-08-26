package com.github.ksoichiro.gag

class StreamGobbler extends Thread {

    def tag
    BufferedReader reader;

    public StreamGobbler(InputStream is, def tag) {
        this.reader = new BufferedReader(new InputStreamReader(is));
        this.tag = tag
    }

    public void run() {
        def tag
        if (this.tag == null) {
            tag = "  "
        } else {
            tag = "  [${this.tag}] "
        }

        // String s causes NoClassDefFoundException for ShortTypeHandling
        def s
        while ((s = this.reader.readLine()) != null) {
            // Just drop contents
            println(tag + s)
        }
    }

}
