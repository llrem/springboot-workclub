package com.yu.service.impl;

import cn.hutool.json.JSONUtil;
import com.aliyun.oss.*;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.yu.common.api.Result;
import com.yu.dto.*;
import com.yu.service.OssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Oss对象存储管理Service实现类
 * Created by macro on 2018/5/17.
 */
@Service
public class OssServiceImpl implements OssService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OssServiceImpl.class);
	@Value("${aliyun.oss.policy.expire}")
	private int ALIYUN_OSS_EXPIRE;
	@Value("${aliyun.oss.maxSize}")
	private int ALIYUN_OSS_MAX_SIZE;
	@Value("${aliyun.oss.callback}")
	private String ALIYUN_OSS_CALLBACK;
	@Value("${aliyun.oss.bucketName}")
	private String ALIYUN_OSS_BUCKET_NAME;
	@Value("${aliyun.oss.endpoint}")
	private String ALIYUN_OSS_ENDPOINT;
	@Value("${aliyun.oss.dir.prefix}")
	private String ALIYUN_OSS_DIR_PREFIX;

	@Autowired
	private OSSClient ossClient;

	/**
	 * 签名生成
	 */
	@Override
	public OssPolicyResult policy() {
		OssPolicyResult result = new OssPolicyResult();
		// 存储目录
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//String dir = ALIYUN_OSS_DIR_PREFIX+sdf.format(new Date());
		String dir = ALIYUN_OSS_DIR_PREFIX;
		// 签名有效期
		long expireEndTime = System.currentTimeMillis() + ALIYUN_OSS_EXPIRE * 1000;
		Date expiration = new Date(expireEndTime);
		// 文件大小
		long maxSize = ALIYUN_OSS_MAX_SIZE * 1024 * 1024;
		// 回调
		OssCallbackParam callback = new OssCallbackParam();
		callback.setCallbackUrl(ALIYUN_OSS_CALLBACK);
		callback.setCallbackBody("filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
		callback.setCallbackBodyType("application/x-www-form-urlencoded");
		// 提交节点
		String action = "http://" + ALIYUN_OSS_BUCKET_NAME + "." + ALIYUN_OSS_ENDPOINT;
		try {
			PolicyConditions policyConds = new PolicyConditions();
			policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
			policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
			String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
			byte[] binaryData = postPolicy.getBytes("utf-8");
			String policy = BinaryUtil.toBase64String(binaryData);
			String signature = ossClient.calculatePostSignature(postPolicy);
			String callbackData = BinaryUtil.toBase64String(JSONUtil.parse(callback).toString().getBytes("utf-8"));
			// 返回结果
			result.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
			result.setPolicy(policy);
			result.setSignature(signature);
			result.setDir(dir);
			result.setCallback(callbackData);
			result.setHost(action);
		} catch (Exception e) {
			LOGGER.error("签名生成失败", e);
		}
		return result;
	}

	@Override
	public OssCallbackResult callback(HttpServletRequest request) {
		OssCallbackResult result= new OssCallbackResult();
		String filename = request.getParameter("filename");
		filename = "http://".concat(ALIYUN_OSS_BUCKET_NAME).concat(".").concat(ALIYUN_OSS_ENDPOINT).concat("/").concat(filename);
		result.setFilename(filename);
		result.setSize(request.getParameter("size"));
		result.setMimeType(request.getParameter("mimeType"));
		result.setWidth(request.getParameter("width"));
		result.setHeight(request.getParameter("height"));
		return result;
	}

	@Override
	public boolean download(String objectName,OutputStream os) {
		try {
			OSSObject object = ossClient.getObject(ALIYUN_OSS_BUCKET_NAME,objectName);
			BufferedInputStream in = new BufferedInputStream(object.getObjectContent());
			BufferedOutputStream out = new BufferedOutputStream(os);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			out.flush();
			out.close();
			in.close();
			return true;

		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public void delete(String objectName) {
		ossClient.deleteObject(ALIYUN_OSS_BUCKET_NAME, objectName);
	}

	@Override
	public void deleteFolder(String objectName){
		// 删除目录及目录下的所有文件。
		String nextMarker = null;
		ObjectListing objectListing = null;
		do {
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ALIYUN_OSS_BUCKET_NAME)
					.withPrefix(objectName)
					.withMarker(nextMarker);

			objectListing = ossClient.listObjects(listObjectsRequest);
			if (objectListing.getObjectSummaries().size() > 0) {
				List<String> keys = new ArrayList<>();
				for (OSSObjectSummary s : objectListing.getObjectSummaries()) {
					System.out.println("key name: " + s.getKey());
					keys.add(s.getKey());
				}
				DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(ALIYUN_OSS_BUCKET_NAME).withKeys(keys).withEncodingType("url");
				DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(deleteObjectsRequest);
				List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
				try {
					for(String obj : deletedObjects) {
						String deleteObj =  URLDecoder.decode(obj, "UTF-8");
						System.out.println(deleteObj);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			nextMarker = objectListing.getNextMarker();
		} while (objectListing.isTruncated());
	}

	@Override
	public FileListParam listObjects(String projectId) {
		String keyPrefix = "workclub/file/project/"+projectId+"/";

		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ALIYUN_OSS_BUCKET_NAME);
		listObjectsRequest.setPrefix(keyPrefix);
		listObjectsRequest.setDelimiter("/");
		// 列举文件。
		ObjectListing listing = ossClient.listObjects(listObjectsRequest);

		List<FileParam> folderList = new ArrayList<>();
		// 遍历所有文件夹
		for (String commonPrefix : listing.getCommonPrefixes()) {
			FileParam fileParam = new FileParam();
			String[] path = Pattern.compile("/").split(commonPrefix);
			fileParam.setName(path[path.length-1]);
			fileParam.setPath(commonPrefix);
			folderList.add(fileParam);
		}
		List<FileParam> fileList = new ArrayList<>();
		// 遍历所有文件
		for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
			if(!objectSummary.getKey().endsWith("/")){
				FileParam fileParam = new FileParam();
				String[] path = Pattern.compile("/").split(objectSummary.getKey());
				fileParam.setName(path[path.length-1]);
				fileParam.setPath(objectSummary.getKey());
				fileList.add(fileParam);
			}
		}
		return new FileListParam(folderList,fileList);
	}

	@Override
	public boolean addFolder(String objectName) {
		byte[] buffer = new byte[0];
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		ossClient.putObject(ALIYUN_OSS_BUCKET_NAME,objectName,in);
		return true;
	}
}
