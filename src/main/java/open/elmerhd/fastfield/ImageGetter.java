package open.elmerhd.fastfield;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * rest api for getting image
 *
 * @author elmerhd
 */
@Stateless
@Path("/image")
public class ImageGetter {

    @GET
    @Produces({"image/jpg","application/json"})
    public Response getImage(@QueryParam("username") String username, @QueryParam("password") String password, @QueryParam("file") String file) throws IOException, Exception {
        Response.ResponseBuilder response = null;
        JSONObject jsonResponse = new JSONObject();
        if (username == null || username.isEmpty()) {
            jsonResponse.put("status", 417);
            jsonResponse.put("messsage", "username is needed.");
            response = Response.status(Response.Status.EXPECTATION_FAILED).entity(jsonResponse.toJSONString()).header("Content-Type", "application/json");
        } else if (password == null || password.isEmpty()) {
            jsonResponse.put("status", 417);
            jsonResponse.put("messsage", "password is needed.");
            response = Response.status(Response.Status.EXPECTATION_FAILED).entity(jsonResponse.toJSONString()).header("Content-Type", "application/json");
        } else if (file == null || file.isEmpty()) {
            jsonResponse.put("status", 417);
            jsonResponse.put("messsage", "file is needed.");
            response = Response.status(Response.Status.EXPECTATION_FAILED).entity(jsonResponse.toJSONString()).header("Content-Type", "application/json");
        } else {
            String fastfieldLogin = String.format("https://manage.fastfieldforms.com/Account/Login?UserName=%s&Password=%s", username, password);
            String imageURL = String.format("https://manage.fastfieldforms.com/api/media/amazon/%s/redirect", file);
            Connection con = Jsoup.connect(fastfieldLogin);
            Document doc = con.post();
            if (validCredentials(doc)) {
                try {
                    Connection.Response resultImageResponse = con.url(imageURL).ignoreContentType(true).execute();
                    response = Response.ok(new ByteArrayInputStream(resultImageResponse.bodyAsBytes())).header("Content-Type", "image/jpg");
                } catch (Exception e) {
                    jsonResponse.put("status", 404);
                    jsonResponse.put("messsage", "File not found");
                    response = Response.status(Response.Status.EXPECTATION_FAILED).entity(jsonResponse.toJSONString()).header("Content-Type", "application/json");
                }
            } else {
                jsonResponse.put("status", 401);
                jsonResponse.put("messsage", "Email or Password is incorrect");
                response = Response.status(Response.Status.UNAUTHORIZED).entity(jsonResponse.toJSONString()).header("Content-Type", "application/json");
            }
        }
        return response.build();
    }

    public boolean validCredentials(Document doc) {
        return !doc.body().toString().contains("Email or Password is incorrect");
    }
}
