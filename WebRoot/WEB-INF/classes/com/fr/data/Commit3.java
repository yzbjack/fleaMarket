package com.fr.data;  

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.cache.Attachment;
import com.fr.data.ClassSubmitJob;
import com.fr.data.SubmitJob;
import com.fr.data.impl.SubmitJobValue;
import com.fr.general.FArray;
import com.fr.general.FRLogManager;
import com.fr.general.FRLogger;
import com.fr.script.Calculator;
import com.fr.stable.UtilEvalError;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;


public class Commit3 implements SubmitJob {
    private Object attach;
    private SubmitJobValue filePath; //�����ļ�·��

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

	public void doJob(Calculator ca) throws Exception {
		FRContext.getLogger().info("begin to upload file...");
		final Object attachO = attach;
		if (attachO instanceof FArray && ((FArray) attachO).length() != 0) {
			new Thread() {
				public void run() {
					int i;
					FArray attachmentlist = (FArray) attachO;
					for (i = 0; i < attachmentlist.length(); i++) {
						if (!(attachmentlist.elementAt(i) instanceof Attachment)) {
							continue;
						} else {
							FRContext.getLogger().info("filePath.value:" + filePath.getValue().toString());
							FRContext.getLogger().info("filePath.valueState:" + filePath.getValueState() +
									"ע��valueState 0,1,2,3 �ֱ��ʾ Ĭ��ֵ�������У�ֵ�ı䣬ɾ����");

							String FilePath = filePath.getValue().toString();
							String FileName = ((Attachment) (attachmentlist.elementAt(i))).getFilename();
							String Path = FilePath + "\\" + FileName;
							File fileDir = new File(FilePath);
							if (!fileDir.exists()) {
								fileDir.mkdirs();
							}
							try {
								mkfile(FilePath, FileName, new java.io.ByteArrayInputStream(
										((Attachment) (attachmentlist.elementAt(i))).getBytes())); //�½��ļ��У�����д����
							} catch (Exception e) {
								Logger.getLogger("FR").log(Level.WARNING,
										e.getMessage() + "/nmkfileerror", e);
							}
						}
					}
				}
			}.start();
		}
	}

    private static void mkfile(String path, String FileName,
            java.io.InputStream source) throws FileNotFoundException,
            IOException {
        File fileout = new File(path, FileName);

        if (fileout.exists()) {// ����Ƿ����
            fileout.delete();// ɾ���ļ�
			FRContext.getLogger().info("old file deleted");
        }
        // �ڵ�ǰĿ¼�½���һ����ΪFileName���ļ�
        if(fileout.createNewFile()) {
			FRContext.getLogger().info(path + FileName + "created!!");
		}
        FileOutputStream outputStream = new FileOutputStream(fileout);
        byte[] bytes = new byte[1024];
        int read=source.read(bytes);
        while (read!=-1) {
            outputStream.write(bytes, 0, read);
             outputStream.flush();
             read = source.read(bytes);
        }
        outputStream.close(); //��sourceд���½����ļ�
    }

    public void readXML(XMLableReader reader) {
        // TODO Auto-generated method stub
    }

    public void writeXML(XMLPrintWriter writer) {
    }

	@Override
	public void doFinish(Calculator arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}