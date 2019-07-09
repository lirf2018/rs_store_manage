package com.yufan.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;

/**
 * 功能名称: 图片处理类
 * 开发人: lirf
 * 开发时间: 2015下午9:37:58
 * 其它说明：
 */
public class ImageUtil {

    private Logger LOG = org.apache.log4j.Logger.getLogger(ImageUtil.class);
    private volatile static ImageUtil imageUtil;

    public static ImageUtil getInstance() {
        if (null == imageUtil) {
            synchronized (ImageUtil.class) {
                if (null == imageUtil) {
                    imageUtil = new ImageUtil();
                }
            }
        }
        return imageUtil;
    }

    /**
     * 根据byte数组，生成文件
     *
     * @param bfile
     * @param filePath
     * @param fileName
     */
    public boolean getFile(byte[] bfile, String filePath, String fileName) {
        try {
            BufferedOutputStream bos = null;
            FileOutputStream fos = null;
            File file = null;
            try {
                File dir = new File(filePath);
                if (!dir.exists() && !dir.isDirectory()) {// 判断文件目录是否存在
                    dir.mkdirs();
                }
                file = new File(filePath + "/" + fileName);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                bos.write(bfile);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 根据 InputStream 数组，生成文件
     *
     * @param in
     * @param filePath
     * @param fileName
     */
    public void getFile(InputStream in, String filePath, String fileName) {
        try {
            BufferedOutputStream bos = null;
            FileOutputStream fos = null;
            File file = null;
            try {
                File dir = new File(filePath);
                if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                    dir.mkdirs();
                }
                file = new File(filePath + "/" + fileName);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                byte[] buffer = new byte[1024];
                int len = 0;
                // 从数据库中读取到指定的字节数组中
                while ((len = in.read(buffer)) != -1) {
                    // 从指定的数组中读取，然后输出来，
                    // 所以这里buffer好象是连接inputStream和outputStream的一个东西
                    bos.write(buffer, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获得指定文件的byte数组
    public byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    // 获得指定文件的byte数组
    public byte[] getBytes(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                // delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    // //删除文件夹
    // //param folderPath 文件夹完整绝对路径
    // public   void delFolder(String folderPath) {
    // try {
    // delAllFile(folderPath); //删除完里面所有内容
    // String filePath = folderPath;
    // filePath = filePath.toString();
    // java.io.File myFilePath = new java.io.File(filePath);
    // myFilePath.delete(); //删除空文件夹
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * Blob写文件
     *
     * @param filePath
     * @param blob
     */
    static void getBlobToFile(String filePath, java.sql.Blob blob) {
        InputStream ins = null;
        OutputStream fout = null;
        try {
            ins = blob.getBinaryStream();
            //输出到文件
            File file = new File(filePath);
            fout = new FileOutputStream(file);
            //将BLOB数据写入文件
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = ins.read(b)) != -1) {
                fout.write(b, 0, len);
            }
            fout.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动文件到指定文件夹
     *
     * @param filePath     文件路径+文件名
     * @param removeToPath 指定文件夹
     */
    public synchronized void removePathImg(String filePath, String removeToPath) {
        try {

            if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(removeToPath)) {
                return;
            }
            filePath = filePath.replace("/", "\\");
            removeToPath = removeToPath.replace("/", "\\");
            //文件名
            String fileNames[] = filePath.split("\\\\");
            String fileName = fileNames[fileNames.length - 1];
            LOG.info("删除的文件名称-->" + fileName);
            File deleteFile = new File(removeToPath);
            if (!deleteFile.exists()) {
                deleteFile.mkdirs();
            }
            File file = new File(filePath);
            File delFile = new File(removeToPath + "\\" + fileName);

            LOG.info("--->移动的文件路径: " + filePath);
            LOG.info("--->移动后的文件路径: " + removeToPath + "\\" + fileName);

            if (null == file || !file.exists()) {
                LOG.info("文件不存在-->" + file);
                return;
            }
            long fileSize = file.length();
            FileOutputStream outputStream = new FileOutputStream(delFile);
            //读或写的字节数
            long writeByte = Files.copy(file.toPath(), outputStream);
            if (writeByte == fileSize) {
                file.delete();
            } else {
                LOG.info("--->删除失败--->path=" + filePath);
            }
            if (null != outputStream) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean removePathImg(String filePath, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty(filePath) || null == request) {
                return false;
            }
            //移动图片
            String path = request.getSession().getServletContext().getRealPath("/");
            String p[] = path.split("webapps");
            String removeToPath = p[0] + "webapps/image/delImg";
            filePath = p[0] + "webapps/image/" + filePath;

            filePath = filePath.replace("/", "\\");
            removeToPath = removeToPath.replace("/", "\\");
            //文件名
            String fileNames[] = filePath.split("\\\\");
            String fileName = fileNames[fileNames.length - 1];
            LOG.info("删除的文件名称-->" + fileName);
            File deleteFile = new File(removeToPath);
            if (!deleteFile.exists()) {
                deleteFile.mkdirs();
            }
            File file = new File(filePath);
            File delFile = new File(removeToPath + "\\" + fileName);

            LOG.info("--->移动的文件路径: " + filePath);
            LOG.info("--->移动后的文件路径: " + removeToPath + "\\" + fileName);

            if (null == file || !file.exists()) {
                LOG.info("文件不存在-->" + file);
                return true;
            }
            long fileSize = file.length();
            FileOutputStream outputStream = new FileOutputStream(delFile);
            //读或写的字节数
            long writeByte = Files.copy(file.toPath(), outputStream);
            if (writeByte == fileSize) {
                file.delete();
            } else {
                LOG.info("--->删除失败--->path=" + filePath);
            }
            if (null != outputStream) {
                outputStream.flush();
                outputStream.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除图片
     *
     * @param filePath
     * @param request
     * @return
     */
    public synchronized boolean deletePathImg(String filePath, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty(filePath) || null == request) {
                return false;
            }
            //删除图片
            String path = request.getSession().getServletContext().getRealPath("/");
            String p[] = path.split("webapps");
            String removeToPath = p[0] + "webapps/image/delImg";
            filePath = p[0] + "webapps/image/" + filePath;

            filePath = filePath.replace("/", "\\");
            removeToPath = removeToPath.replace("/", "\\");
            //文件名
            String fileNames[] = filePath.split("\\\\");
            String fileName = fileNames[fileNames.length - 1];
            LOG.info("删除的文件名称-->" + fileName);
            File deleteFile = new File(removeToPath);
            if (!deleteFile.exists()) {
                deleteFile.mkdirs();
            }
            File file = new File(filePath);
            File delFile = new File(removeToPath + "\\" + fileName);

            LOG.info("--->删除的文件路径: " + filePath);
            LOG.info("--->删除的文件路径: " + removeToPath + "\\" + fileName);

            if (file.exists()) {
                file.delete();
            }
            if (delFile.exists()) {
                delFile.delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
