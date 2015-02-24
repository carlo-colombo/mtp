package mtp.controllers;

import java.util.Collection;

import javax.websocket.server.PathParam;

import mtp.dataobjects.Entry;
import mtp.services.DatastoreService;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class ApiController {
	@Autowired
	DatastoreService datastoreService;

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public @ResponseBody String post(@RequestBody Entry entry) {
		return datastoreService.put(entry);
	}

	@RequestMapping(value = "/list")
	public @ResponseBody Collection<Entry> list() {
		return datastoreService.list();
	}

	@RequestMapping(value = "/view/{viewName}")
	public @ResponseBody Object view(@PathVariable String viewName,
			@RequestParam(required = false) Integer groupLevel,
			@RequestParam(required = false) Boolean group) {
		return datastoreService.getView(viewName, BooleanUtils.isTrue(group),
				groupLevel);
	}
}
