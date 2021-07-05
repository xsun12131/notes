package com.fatpanda.notes.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.text.DecimalFormat;

/**
 * @author fatPanda
 * @date 2020/11/18
 */
public class FileUtil {

    public static void main(String[] args) throws IOException {
        String apijson = readFile(new File("/home/tykj/apijson.txt"));
        // String apijson = readFile(new File("/home/tykj/test.txt"));
        // Object parse = JSONObject.parse(apijson);
        System.out.println(apijson);
    }

    public static String getMD5(String filePath) throws IOException {
        return DigestUtils.md5Hex(new FileInputStream(filePath));
    }

    /**
     * 创建文件夹
     *
     * @param filePath
     */
    public static Boolean createDirs(String filePath) {

        if (filePath.lastIndexOf(".") > 0) {
            filePath = filePath.substring(0, filePath.lastIndexOf(File.separator));
        }

        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return true;
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String sPath) {
        File file = new File(sPath);
        // 判断目录或文件是否存在
        // 不存在返回 false
        if (!file.exists()) {
            return false;
        } else {
            // 判断是否为文件
            // 为文件时调用删除文件方法
            if (file.isFile()) {
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            flag = file.delete();
        }
        file = null;
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        Boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字节转B,KB,MB,GB
     *
     * @param size
     * @return
     */
    public static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    /**
     * 递归方式 计算文件的大小
     * @param file
     * @return
     */
    public static long getTotalSizeOfFilesInDir(File file) {
        if (file.isFile()) {
            return file.length();
        }

        File[] children = file.listFiles();
        long total = 0;
        if (children != null) {
            for (File child : children) {
                total += getTotalSizeOfFilesInDir(child);
            }
        }
        return total;
    }

    public static String readFile(File file) {
        FileInputStream in = null;
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String strLine = null;
            while(null != (strLine = bufferedReader.readLine())){
                sb.append(strLine);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }

    public static String readFile(MultipartFile multipartFile) {
        FileInputStream in = null;
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
            String strLine = null;
            while(null != (strLine = bufferedReader.readLine())){
                sb.append(strLine + "\n");
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }

    private static MimetypesFileTypeMap mtftp = new MimetypesFileTypeMap();

    public static boolean isImage(File file) {
        /* 不添加下面的类型会造成误判 详见：http://stackoverflow.com/questions/4855627/java-mimetypesfiletypemap-always-returning-application-octet-stream-on-android-e*/
        mtftp.addMimeTypes("image png tif jpg jpeg bmp");
        String mimetype = mtftp.getContentType(file);
        String type = mimetype.split("/")[0];
        return type.equals("image");
    }


}
