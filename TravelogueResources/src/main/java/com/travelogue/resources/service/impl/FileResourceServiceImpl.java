package com.travelogue.resources.service.impl;

import java.io.IOException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSFile;
import com.travelogue.resources.exception.ResourceException;
import com.travelogue.resources.service.FileResourceService;
import com.travelogue.resources.web.rest.dto.FileDTO;

@Service
public class FileResourceServiceImpl implements FileResourceService {

	private final Logger log = LoggerFactory.getLogger(FileResourceServiceImpl.class);

	@Inject
	private GridFsTemplate gridFsTemplate;

	public Object createFile(MultipartFile file, FileDTO fileDTO) {
		DBObject metaData = new BasicDBObject();
		metaData.put("user", "alex");
		GridFSFile gfsFile = null;
		try {
			gfsFile = gridFsTemplate.store(file.getInputStream(), metaData);
		} catch (IOException e) {
			log.error("IOException occured processing the Create Resource ");
			throw new ResourceException(e);
		}
		return gfsFile.getId();
	}

}
