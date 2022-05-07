package nl.sikken.bertrik.hab.habitat;

import java.time.Instant;
import java.util.Arrays;

import nl.sikken.bertrik.hab.Location;
import nl.sikken.bertrik.hab.Sentence;

public final class RunMultiReceiverTest {
    
    public static void main(String[] args) {
        RunMultiReceiverTest test = new RunMultiReceiverTest();
        test.run();
    }

    private void run() {
        HabitatConfig config = new HabitatConfig("http://habitat.habhub.org", 30);
        HabitatUploader uploader = HabitatUploader.create(config);
        uploader.start();
        try {
            Instant now = Instant.now();
            Sentence sentence = new Sentence("TTNTEST", 1, now);
            HabReceiver receiver1 = new HabReceiver("BERTRIK1", new Location(52.022887, 4.69, 0.0));
            HabReceiver receiver2 = new HabReceiver("BERTRIK2", new Location(52.022887, 4.70, 0.0));
            
            uploader.schedulePayloadTelemetryUpload(sentence.format(), Arrays.asList(receiver1, receiver2), now);
        } finally {
            uploader.stop();
        }
    }

}
