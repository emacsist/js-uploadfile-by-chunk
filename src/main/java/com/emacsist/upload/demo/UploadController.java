package com.emacsist.upload.demo;

import com.google.common.io.ByteStreams;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

@RestController
public class UploadController {

    @RequestMapping(value = "/merge", method = RequestMethod.POST)
    public String upload(String name, String md5) throws IOException {
        FileOutputStream dos = new FileOutputStream("/tmp/" + name);
        //merge
        File dir = new File("/tmp/" + md5);
        File[] list = dir.listFiles();

        Arrays.sort(list, Comparator.comparingInt(f -> Integer.parseInt(f.getName())));
        System.out.println("order => " + list);
        byte[] buf = new byte[1024];
        for (File f : list) {
            InputStream inputStream = new FileInputStream(f);
            int len = 0;
            while ((len = inputStream.read(buf)) != -1) {
                dos.write(buf, 0, len);
            }
            inputStream.close();
        }
        dos.close();
        for (File f : list) {
            f.delete();
        }
        dir.delete();

        return "ok";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(HttpServletRequest request, int chunks, int nth, long start, long end, @RequestParam("fileName") String name, @RequestParam("fileSize") long totalLen, String md5) throws IOException {
        byte[] bodys = ByteStreams.toByteArray(request.getInputStream());
        System.out.println("size > " + bodys.length);

        final String dirName = "/tmp/" + md5;
        File dir = new File(dirName);
        dir.mkdirs();


        System.out.println(md5 + ", [" + start + " , " + end + "]" + " total " + totalLen);
        File file = new File(dirName + "/" + nth);
        //File dir = new File("/tmp/");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bodys);
        fos.flush();
        fos.close();
        return "ok";
    }
}
