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

    @Override
    protected Application configure() {
        return new ResourceConfig(ImageGetter.class);
    }

    @Test
    public void usernameTest() {
        Response response = target("image/").queryParam("username", "").request().get(Response.class);
        Response.ResponseBuilder expectedResponse = Response.status(Response.Status.EXPECTATION_FAILED);
        Assert.assertTrue(expectedResponse.build().getStatus() == response.getStatus());
    }
    @Test
    public void passwordTest() {
        Response response = target("image/").queryParam("username", "").queryParam("password", "").request().get(Response.class);
        Response.ResponseBuilder expectedResponse = Response.status(Response.Status.EXPECTATION_FAILED);
        Assert.assertTrue(expectedResponse.build().getStatus() == response.getStatus());
    }
    @Test
    public void fileTest() {
        Response response = target("image/").queryParam("username", "").queryParam("password", "").queryParam("file", "").request().get(Response.class);
        Response.ResponseBuilder expectedResponse = Response.status(Response.Status.EXPECTATION_FAILED);
        Assert.assertTrue(expectedResponse.build().getStatus() == response.getStatus());
    }
    @Test
    public void InvalidUsernameOrPasswordTest() {
        Response response = target("image/").queryParam("username", "elmerhd").queryParam("password", "mypassword").queryParam("file", "file.jpg").request().get(Response.class);
        Response.ResponseBuilder expectedResponse = Response.status(Response.Status.UNAUTHORIZED);
        Assert.assertTrue(expectedResponse.build().getStatus() == response.getStatus());
    }
}
