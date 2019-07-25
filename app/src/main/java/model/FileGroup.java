package model;

import cn.bmob.v3.BmobObject;

import java.io.File;
import java.io.Serializable;

public class FileGroup extends BmobObject implements Serializable {
    private int paperType;
    private String fileType;
    private StudentInfo student;
    private int fileOpinion;
    private String fileName;
    private String filePath;
    private File file;

    public int getPaperType() {
        return paperType;
    }

    public void setPaperType(int paperType) {
        this.paperType = paperType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public StudentInfo getStudent() {
        return student;
    }

    public void setStudent(StudentInfo student) {
        this.student = student;
    }

    public int getFileOpinion() {
        return fileOpinion;
    }

    public void setFileOpinion(int fileOpinion) {
        this.fileOpinion = fileOpinion;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileGroup{" +
                "paperType=" + paperType +
                ", fileType='" + fileType + '\'' +
                ", fileOpinion=" + fileOpinion +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", file=" + file +
                ", student=" + student +
                '}';
    }
}
