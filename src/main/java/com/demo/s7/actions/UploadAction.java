package com.demo.s7.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ActionSupport;
import org.apache.struts2.action.UploadedFilesAware;
import org.apache.struts2.dispatcher.multipart.UploadedFile;
import org.apache.struts2.interceptor.httpmethod.HttpPost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.demo.s7.R;

/***
 * struts6.0+ 需要实现UploadedFilesAware接口
 */
@Controller
@Scope("prototype")
@HttpPost
public class UploadAction extends ActionSupport implements UploadedFilesAware {

	private static final long serialVersionUID = 1L;
	private transient Map<String, Object> result;
	private transient Map<String, Object> errorResult;

	@Value("${savePath}")
	private String savePath;

	@Value("${visitUrlPrifix}")
	private String visitUrlPrifix;

	// 多文件用数组接收
	private UploadedFile[] uploadFiles = {};

	public String page() {
		return SUCCESS;
	}

	public String file() {
		// 遍历保存多个文件
		List<String> lst = new ArrayList<>();
		for (UploadedFile uploadedFile : uploadFiles) {
			String name = uploadedFile.getAbsolutePath().replace(".tmp", ".png");
			try {
				File saveFile = new File(name);
				FileUtils.moveFile(new File(uploadedFile.getAbsolutePath()), new File(name));
				lst.add(visitUrlPrifix + FilenameUtils.getName(saveFile.getName()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		setResult(R.success(lst));
		return SUCCESS;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public Map<String, Object> getErrorResult() {
		return errorResult;
	}

	public void setErrorResult(Map<String, Object> errorResult) {
		this.errorResult = errorResult;
	}

	@Override
	public void withUploadedFiles(List<UploadedFile> uploadedFiles) {
		uploadFiles = uploadedFiles.toArray(uploadFiles);
	}

}
