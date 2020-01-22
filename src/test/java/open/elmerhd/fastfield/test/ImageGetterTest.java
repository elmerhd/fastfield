package open.elmerhd.fastfield.test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import open.elmerhd.fastfield.ImageGetter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author elmerhd
 */
public class ImageGetterTest extends JerseyTest {
    /**
     * Configure server
     * @return the configured server
     */
    @Override
    protected Application configure() {
        return new ResourceConfig(ImageGetter.class);
    }
    /**
     * Test on password
     * expected {@link javax.ws.rs.core.Response.Status} EXPECTATION_FAILED
     */
    @Test
    public void fileTest() {
        Response response = target("image/").queryParam("file", "").request().get(Response.class);
        Response.ResponseBuilder expectedResponse = Response.status(Response.Status.EXPECTATION_FAILED);
        Assert.assertTrue(expectedResponse.build().getStatus() == response.getStatus());
    }
}
