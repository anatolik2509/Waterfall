package ru.itis.antonov.waterfall.servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.itis.antonov.waterfall.services.ArticleService;
import ru.itis.antonov.waterfall.services.MediaService;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@WebServlet("/media")
public class MediaServlet extends HttpServlet {
    private ServletContext context;
    private MediaService mediaService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = config.getServletContext();
        mediaService = (MediaService) context.getAttribute("mediaService");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("path") == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String path = req.getParameter("path");
        resp.setHeader("Content-Type", mediaService.getMimeType(path));
        InputStream in = mediaService.getFile(path);
        OutputStream out = resp.getOutputStream();
        byte[] buffer = new byte[1024];
        int i;
        while ((i = in.read(buffer)) != -1){
            out.write(buffer);
        }
        in.close();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (!isMultipart) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();

        File tempDir = (File)context.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(tempDir);
        ServletFileUpload upload = new ServletFileUpload(factory);
        Writer wr = resp.getWriter();
        try {
            List<FileItem> items = upload.parseRequest(req);
            for(FileItem i : items){
                wr.write(mediaService.saveFileItem(i));
            }
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }
        wr.close();
    }
}
