/**
 * 
 */
package net.sourceforge.gjtapi.raw.sipprovider.media;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import net.sourceforge.gjtapi.raw.sipprovider.common.NetworkAddressManager;

import org.junit.Test;

/**
 * @author ds01191
 *
 */
public class MediaManagerTest {

    /**
     * Test method for {@link net.sourceforge.gjtapi.raw.sipprovider.media.MediaManager#play(java.lang.String)}.
     * @exception Exception
     *            test failed
     */
    @Test
    public void testPlay() throws Exception {
        Properties props1 = new Properties();
        InputStream in1 = MediaManagerTest.class.getResourceAsStream(
                "phone1.properties");
        props1.load(in1);
        NetworkAddressManager addressManager = new NetworkAddressManager();
        addressManager.init(props1);
        Properties props2 = new Properties();
        InputStream in2 = MediaManagerTest.class.getResourceAsStream(
            "phone1.properties");
        props2.load(in2);
        MediaManager recordManager = new MediaManager(props1, addressManager);
        recordManager.start();
        recordManager.record("file:out.wav");
        MediaManager playManager = new MediaManager(props1, addressManager);
        playManager.start();
        String sdp = playManager.generateSdpDescription();
        playManager.openMediaStreams(sdp);
        File file = new File("demo/gui/test.wav");
        playManager.play(file.toURL().toString());
        Thread.sleep(3000);
    }

}
