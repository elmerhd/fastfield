package open.elmerhd.fastfield;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.ejb.Stateless;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
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
    /**
     * Get the image from fastfield
     * @param file the name of the file
     * @return image/jpg if success else return application/json
     * @throws Exception
     */
    @GET
    @Produces({"image/jpg", "application/json"})
    public Response getImage(@QueryParam("file") String file) throws Exception {
        Response.ResponseBuilder response = null;
        JSONObject jsonResponse = new JSONObject();
        if (file == null || file.isEmpty()) {
            jsonResponse.put("status", 417);
            jsonResponse.put("messsage", "file is needed.");
            response = Response.status(Response.Status.EXPECTATION_FAILED).entity(jsonResponse.toJSONString()).header("Content-Type", "application/json");
        } else {
            SSLSocketFactory sslSocketFactory = getSSLSocketFactory();
            
            String username = URLEncoder.encode(System.getProperty("fastfield-username"), "UTF-8");
            String password = URLEncoder.encode(System.getProperty("fastfield-password"), "UTF-8");
            
            String fastfieldLogin = String.format("https://manage.fastfieldforms.com/Account/Login?UserName=%s&Password=%s", username, password);
            String imageURL = String.format("https://manage.fastfieldforms.com/api/media/amazon/%s/redirect", file);
            Connection con = Jsoup.connect(fastfieldLogin).sslSocketFactory(sslSocketFactory);
            Document doc = con.post();
            if (validCredentials(doc)) {
                try {
                    Connection.Response resultImageResponse = con.url(imageURL).sslSocketFactory(sslSocketFactory).ignoreContentType(true).execute();
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

    /**
     * Validate if body contains Email or Password is incorrect
     *
     * @param doc the document
     * @return true if doesn't contains else false
     */
    public boolean validCredentials(Document doc) {
        return !doc.body().toString().contains("Email or Password is incorrect");
    }
    /**
     * Get secured SSLSocket from trust managers
     * @return
     * @throws Exception 
     */
    private static SSLSocketFactory getSSLSocketFactory() throws Exception {
        TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                
            }
        }};
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, byPassTrustManagers, new SecureRandom());
        return sslContext.getSocketFactory();
    }

}
