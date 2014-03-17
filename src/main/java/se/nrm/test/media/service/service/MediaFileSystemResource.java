package se.nrm.test.media.service.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import se.nrm.test.media.util.PathHelper;

/**
 *
 * @author ingimar
 */

@Path("/media")
@Produces("image/png")
public class MediaFileSystemResource {

    @GET
    @Path("/stream/{uuid}")
    public Response getMedia(@PathParam("uuid") String fileName) {
        String dynPath = getDynamicPath(fileName);
        String file = dynPath.concat(fileName);

        File repositoryFile = new File(file);
        return returnFile(repositoryFile);
    }
    
    private static Response returnFile(File file) {

        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try {
            String mimeType = getMimeType(file);
            return Response.ok(new FileInputStream(file), mimeType).build();
        } catch (IOException ioEx) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/stream/image/{uuid}")
    @Produces("image/jpeg")
    public byte[] getImage(@PathParam("uuid") String uuid, @Context UriInfo info) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);

        try {
            BufferedImage transformedImage = getTransformedImage(info, uuid);
            ImageIO.write(transformedImage, "jpeg", outputStream);
        } catch (IOException ex) {
            Logger.getLogger(MediaFileSystemResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outputStream.toByteArray();
    }

    @GET
    @Path("/stream/image/")
    @Produces("image/jpeg")
    public byte[] getImage(@Context UriInfo info) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);

        try {
            BufferedImage transformedImage = getTransformedImage(info);
            ImageIO.write(transformedImage, "jpeg", outputStream);
        } catch (IOException ex) {
            Logger.getLogger(MediaFileSystemResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outputStream.toByteArray();
    }

    /**
     * Default values : 150x150
     * 
     * Can this be used, is the syntax ok - with '?' in front of uuid
     * http://localhost:8080/MediaServerResteasy/media/stream/image/?uuid=18ac3829-49bd-42ed-a975-0ba839167f33&width=500&height=400
     * @param info
     * @return
     * @throws IOException 
     */
    private BufferedImage getTransformedImage(UriInfo info) throws IOException {
        String uuid = info.getQueryParameters().getFirst("uuid");
        String height = info.getQueryParameters().getFirst("height");
        String width = info.getQueryParameters().getFirst("width");
        int h = 150, w = 150;
        if (height != null && width != null) {
            h = Integer.parseInt(height);
            w = Integer.parseInt(width);
            if ( h < 100 ){
                h= 150;
            }
            if ( w < 100 ){
                w= 150;
            }
        }
        System.out.println("uuid " + uuid);
        String dynPath = getDynamicPath(uuid);
        String filename = dynPath.concat(uuid);

        File fileHandle = new File(filename);
        BufferedImage originalImage = ImageIO.read(fileHandle);

        BufferedImage asBufferedImage = Thumbnails.of(originalImage).size(h, w).asBufferedImage();
        return asBufferedImage;

    }

    /**
     * better syntax :
     * http://localhost:8080/MediaServerResteasy/media/stream/image/?uuid=18ac3829-49bd-42ed-a975-0ba839167f33&width=500&height=400
     * 
     * @param info
     * @param uuid
     * @return
     * @throws IOException 
     */
    private BufferedImage getTransformedImage(UriInfo info, String uuid) throws IOException {
        String height = info.getQueryParameters().getFirst("height");
        String width = info.getQueryParameters().getFirst("width");
        int h = 150, w = 150;
        if (height != null && width != null) {
            h = Integer.parseInt(height);
            w = Integer.parseInt(width);
            if ( h < 100 ){
                h= 150;
            }
            if ( w < 100 ){
                w= 150;
            }
        }
        String dynPath = getDynamicPath(uuid);
        String filename = dynPath.concat(uuid);

        File fileHandle = new File(filename);
        BufferedImage originalImage = ImageIO.read(fileHandle);

        BufferedImage asBufferedImage = Thumbnails.of(originalImage).size(h, w).asBufferedImage();
        return asBufferedImage;

    }

    private String getDynamicPath(String uuid) {
        return PathHelper.getDyanmicPathToFile(uuid);
    }

    private static String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file);
        return mimeType;
    }
}
