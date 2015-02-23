package mtp.services

import java.time.LocalDateTime;

import mtp.dataobjects.Entry
import spock.lang.Specification


class DatastoreServiceImplSpec extends Specification{
	def '#put should return the entry id'(){
		setup:
			def service = new DatastoreServiceImpl(new HashMap<String, Entry>());
			def date = LocalDateTime.now()
			def userId = 1212
			def entryId = service.put(new Entry(userId:userId,timePlaced:date))
		expect:			
			entryId.contains("$date") 
			entryId.contains("$userId")
	}
	
	def '#put should add the calculated id to the entry'(){
		setup:
			def date = LocalDateTime.now()
			def userId = 1212
		expect:
			def service = new DatastoreServiceImpl(new HashMap<String, Entry>());
			def entryId = service.put(new Entry(userId:userId,timePlaced:date))
			service.list().find({
				it.id == entryId
			})!=null
	}
	
	def '#list should return a list of all entries added'(){
		setup:
			def service = new DatastoreServiceImpl(new HashMap<String, Entry>());
			service.put(new Entry(userId:10, timePlaced:LocalDateTime.now()))
			service.put(new Entry(userId:12, timePlaced:LocalDateTime.now()))
			def list = service.list();
		expect:
			list.size()==2
	}
}
