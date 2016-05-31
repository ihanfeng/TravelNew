package com.travelogue.resources.service;

import org.springframework.web.multipart.MultipartFile;

import com.travelogue.resources.web.rest.dto.FileDTO;

public interface FileResourceService {
	
	public Object createFile(MultipartFile file, FileDTO fileDTO);

}
