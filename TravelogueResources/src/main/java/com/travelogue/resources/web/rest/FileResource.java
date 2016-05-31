package com.travelogue.resources.web.rest;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.travelogue.resources.service.FileResourceService;
import com.travelogue.resources.web.rest.dto.FileDTO;

/**
 * Controller for view and managing File Resources required.
 */
@RestController
@RequestMapping("/api")
public class FileResource {

	private final Logger log = LoggerFactory.getLogger(FileResource.class);
	
	@Inject
	private FileResourceService fileResourceService;

	@RequestMapping(value = "/resource", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<?> getResources(@RequestBody FileDTO fileDto ) {
		log.debug("REST request to get Resource : {}", fileDto);

		return null;
	}
	
	@RequestMapping(value = "/resource", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Object> createResources(@RequestParam("file") MultipartFile file, @RequestBody FileDTO fileDto ) {
		log.debug("REST request to post Resource : {}", fileDto);
		return new ResponseEntity<Object>(fileResourceService.createFile(file, fileDto), HttpStatus.CREATED);
	}

}
